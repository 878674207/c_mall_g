package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.delayqueue.enums.RedisDelayQueueEnum;
import com.ruoyi.common.delayqueue.util.RedisDelayQueueUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.tob.converter.OrderConverter;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.qo.SkuStockQo;
import com.ruoyi.tob.service.SkuStockService;
import com.ruoyi.tob.service.impl.ProductServiceImpl;
import com.ruoyi.tob.vo.OrderVo;
import com.ruoyi.toc.entity.BasketItem;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.entity.OrderAddress;
import com.ruoyi.toc.entity.OrderItem;
import com.ruoyi.toc.mapper.BasketItemMapper;
import com.ruoyi.toc.mapper.OrderAddressMapper;
import com.ruoyi.toc.mapper.OrderItemMapper;
import com.ruoyi.toc.mapper.OrderMapper;
import com.ruoyi.toc.qo.OrderAddressQo;
import com.ruoyi.toc.qo.OrderQo;
import com.ruoyi.toc.qo.OrderQueryQo;
import com.ruoyi.toc.service.CustomerBasketService;
import com.ruoyi.toc.service.CustomerDeliveryAddressService;
import com.ruoyi.toc.service.CustomerOrderService;
import com.ruoyi.toc.vo.ConfirmOrder;
import com.ruoyi.toc.vo.ConfirmOrderItem;
import com.ruoyi.toc.vo.ConfirmOrderVo;
import com.ruoyi.toc.vo.PaymentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final String BASKET = "basket";

    private final String MALLREPEATEDSUBMISSIONKEY = "MALL_REPEATED_SUBMISSION_KEY";

    @Autowired
    private SkuStockService skuStockService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BasketItemMapper basketItemMapper;

    @Autowired
    private CustomerBasketService customerBasketService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public void submitOrder(OrderQo orderQo) throws CommonException {
        List<ConfirmOrder> confirmOrderList = orderQo.getConfirmOrderList();
        if (CollectionUtils.isEmpty(confirmOrderList)) {
            throw new CommonException("商品为空，无法提交订单");
        }

        // 订单防抖
        checkRepeatedSubmission(confirmOrderList);

        // 校验库存，并锁定商品库存
        lockSkuStock(confirmOrderList);

        confirmOrderList.forEach(item -> createOrder(item, orderQo.getOrderAddress()));


        // 从购物车发起的订单提交需要删除购物车
        if (BASKET.equals(orderQo.getSettleFrom())) {
            handBasket(orderQo);
        }
    }

    @Override
    public void cancelOrder(Long id) throws CommonException {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在");
        }
        if (!Constants.MALL_ORDER_STATUS_1.equals(order.getOrderStatus())) {
            throw new CommonException("订单状态有误");
        }

        // 解锁库存
        unLockSkuStock(order);

        // todo 后续接入支付，还需要取消支付那边的订单

        // 删除延迟队列原生
        redisDelayQueueUtil.removeDelayedQueue(order.getOrderNo(), RedisDelayQueueEnum.MALL_ORDER_PAYMENT_TIMEOUT.getCode());

        // 更新订单状态
        orderMapper.updateById(new Order().setId(order.getId()).setOrderStatus(Constants.MALL_ORDER_STATUS_5));
    }

    public void unLockSkuStock(Order order) throws CommonException {
        if (Objects.isNull(order)) {
            return;
        }
        List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        if (CollectionUtils.isEmpty(orderItemList)) {
            return;
        }

        List<SkuStockQo> skuStockQoList = Lists.newArrayList();
        for(OrderItem orderItem : orderItemList) {
            skuStockQoList.add(new SkuStockQo().setProductSkuId(orderItem.getProductSkuId()).setUnlockQuantity(orderItem.getQuantity()));
        }
        skuStockService.unLockSkuStock(skuStockQoList);
    }

    @Override
    public Page<Order> myOrderList(OrderQueryQo orderQueryQo) {
        if (StringUtils.isNotBlank(orderQueryQo.getKeyword())) {
            List<OrderItem> orderItemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                    .like(OrderItem::getProductName, orderQueryQo.getKeyword()));
            if (CollectionUtils.isEmpty(orderItemList)) {
                return orderQueryQo.initPage();
            }
            orderQueryQo.setOrderIdList(orderItemList.stream().map(OrderItem::getOrderId).collect(Collectors.toList()));
        }

        Page<Order> page = orderMapper.selectPage(orderQueryQo.initPage(), new LambdaQueryWrapper<Order>()
                .eq(Order::getCustomerId, SecurityUtils.getCurWechatLoginUserId())
                .eq(Order::getDeleteStatus, 0)
                .eq(StringUtils.isNotEmpty(orderQueryQo.getOrderStatus()) , Order::getOrderStatus, orderQueryQo.getOrderStatus())
                .in(CollectionUtils.isNotEmpty(orderQueryQo.getOrderIdList()), Order::getId, orderQueryQo.getOrderIdList())
                .orderByDesc(Order::getCreateTime));

        List<Order> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page;
        }

        List<Long> orderIds = records.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().in(OrderItem::getOrderId, orderIds));
        if (CollectionUtils.isEmpty(orderItemList)) {
            return page;
        }

        records.forEach(item -> item.setOrderItemList(orderItemList.stream()
                .filter(subitem -> subitem.getOrderId().equals(item.getId())).collect(Collectors.toList())));

        return page;
    }

    @Override
    public void completeOrder(String orderNo) throws CommonException {
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo).last("limit 1"));
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在");
        }

        // 只有订单状态处于待支付状态才处理
        if (!Constants.MALL_ORDER_STATUS_1.equals(order.getOrderStatus())) {
            return;
        }

        // todo 插入支付记录

        // 修改订单状态
        orderMapper.updateById(new Order().setId(order.getId()).setOrderStatus(Constants.MALL_ORDER_STATUS_2));

        // 删除延迟队列元素
        redisDelayQueueUtil.removeDelayedQueue(orderNo, RedisDelayQueueEnum.MALL_ORDER_PAYMENT_TIMEOUT.getCode());


        // 修改该订单下商品的销量
        updateProductSale(order.getId());
    }

    @Override
    public void deleteOrder(Long id) throws CommonException {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在");
        }
        orderMapper.updateById(new Order().setId(id).setDeleteStatus(1));
    }

    @Override
    public void updateAddress(OrderAddress orderAddress) {
        orderAddress.setId(null);
        orderAddressMapper.update(orderAddress, new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, orderAddress.getOrderId()));
    }

    @Override
    public Object queryOrderDetail(Long id) throws CommonException {

        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在!");
        }
        OrderVo orderVo = OrderConverter.INSTANCE.toOrderVo(order);
        orderVo.setOrderItemList(orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)));
        orderVo.setOrderAddress(orderAddressMapper.selectOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, id)));
        return orderVo;
    }

    @Override
    public ConfirmOrderVo purchaseAgain(Long id) throws CommonException {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在!");
        }
        ConfirmOrderVo confirmOrderVo = new ConfirmOrderVo();
        confirmOrderVo.setOrderAddress(customerDeliveryAddressService.queryMyDefaultAddress());

        ConfirmOrder confirmOrder = new ConfirmOrder().setStoreId(order.getStoreId()).setStoreName(order.getStoreName());
        List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new CommonException("订单不存在商品!");
        }
        List<ConfirmOrderItem> confirmOrderItemList = Lists.newArrayList();
        orderItemList.forEach(item -> {
            BigDecimal productTotalPrice = item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            ConfirmOrderItem confirmOrderItem = new ConfirmOrderItem()
                    .setProductId(item.getProductId())
                    .setProductSkuId(item.getProductSkuId())
                    .setQuantity(item.getQuantity())
                    .setPrice(item.getProductPrice())
                    .setTotalPrice(productTotalPrice)
                    .setProductPic(item.getProductPic())
                    .setProductName(item.getProductName())
                    .setProductSkuCode(item.getProductSkuCode())
                    .setProductCategoryId(item.getProductCategoryId())
                    .setProductSn(item.getProductSn())
                    .setProductAttr(item.getProductAttr());
            confirmOrderItemList.add(confirmOrderItem);
        });

        confirmOrder.setConfirmOrderItems(confirmOrderItemList);
        confirmOrderVo.setConfirmOrderList(Arrays.asList(confirmOrder));

        // 支付信息 其中运费暂时写死为0
        PaymentVo paymentVo = new PaymentVo();
        paymentVo.setDeliverPrice(BigDecimal.ZERO);
        paymentVo.setProductTotalPrice(confirmOrderItemList.stream().map(ConfirmOrderItem::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add));
        paymentVo.setTotalPrice(paymentVo.getProductTotalPrice().add(paymentVo.getDeliverPrice()));
        confirmOrderVo.setPaymentVo(paymentVo);

        return confirmOrderVo;
    }

    @Override
    public void confirmReceive(Long id) throws CommonException {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在!");
        }
        orderMapper.updateById(new Order().setId(id).setOrderStatus(Constants.MALL_ORDER_STATUS_4));
    }

    private void updateProductSale(Long orderId) {
        List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        if (CollectionUtils.isEmpty(orderItemList)) {
            return;
        }
        Map<Long, Long> quantityMap = orderItemList.stream().collect(Collectors.toMap(OrderItem::getId, OrderItem::getQuantity));
        List<Long> productIds = orderItemList.stream().map(OrderItem::getProductId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(productIds)) {
            return;
        }
        List<Product> productList = productService.list(new LambdaQueryWrapper<Product>().in(Product::getId, productIds));
        List<Product> updateList = Lists.newArrayList();
        productList.forEach(item -> updateList.add(new Product().setId(item.getId()).setSale(item.getSale().longValue() + quantityMap.get(item.getId()).longValue())));
        productService.updateBatchById(updateList);
    }

    /**
     *  防止重复提交
     * @param confirmOrderList
     * @throws CommonException
     */
    private void checkRepeatedSubmission(List<ConfirmOrder> confirmOrderList) throws CommonException {
        String productSkuIdStr = confirmOrderList.stream().map(ConfirmOrder::getConfirmOrderItems)
                .flatMap(Collection::stream).map(item -> item.getProductSkuId().toString()).collect(Collectors.joining(","));
        String redisKey = MALLREPEATEDSUBMISSIONKEY + SecurityUtils.getCurWechatLoginUserId() + productSkuIdStr;
        String isSubmitted = redisCache.getCacheObject(redisKey);
        if (StringUtils.isEmpty(isSubmitted)) {
            redisCache.setCacheObject(redisKey, "isSubmitted", 3, TimeUnit.SECONDS);
            return;
        }
        throw new CommonException("订单已提交请勿重复操作");
    }

    private void handBasket(OrderQo orderQo) {
        List<ConfirmOrder> confirmOrderList = orderQo.getConfirmOrderList();
        if (CollectionUtils.isEmpty(confirmOrderList)) {
            return;
        }
        List<Long> basketItemIds = confirmOrderList.stream().map(ConfirmOrder::getConfirmOrderItems)
                .flatMap(Collection::stream).map(ConfirmOrderItem::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(basketItemIds)) {
            return;
        }
        basketItemMapper.deleteBatchIds(basketItemIds);

        customerBasketService.refreshBasket();
    }

    private void createOrder(ConfirmOrder confirmOrder, OrderAddressQo orderAddressQo) {
        List<ConfirmOrderItem> confirmOrderItems = confirmOrder.getConfirmOrderItems();
        if (CollectionUtils.isEmpty(confirmOrderItems)) {
            return;
        }

        // 计算该订单所有商品的总和
        String orderNo = getOrderNo(confirmOrder.getStoreId());
        BigDecimal totalAmount = confirmOrderItems.stream().map(ConfirmOrderItem::getTotalPrice).reduce(BigDecimal::add).get();
        Order order = new Order()
                .setCustomerId(SecurityUtils.getCurWechatLoginUserId())
                .setStoreId(confirmOrder.getStoreId())
                .setStoreName(confirmOrder.getStoreName())
                .setRemark(confirmOrder.getRemark())
                .setOrderNo(orderNo)
                .setTotalAmount(totalAmount)
                .setPayAmount(totalAmount)
                .setOrderStatus(Constants.MALL_ORDER_STATUS_1)
                .setDeleteStatus(0)
                .setCreateTime(DateUtils.getNowDate());

        // 保存订单主表信息
        orderMapper.insert(order);

        List<OrderItem> orderItemList = Lists.newArrayList();
        confirmOrderItems.forEach(item -> {
            OrderItem orderItem = new OrderItem()
                    .setOrderId(order.getId())
                    .setProductId(item.getProductId())
                    .setProductSkuId(item.getProductSkuId())
                    .setQuantity(item.getQuantity())
                    .setProductPrice(item.getPrice())
                    .setProductPic(item.getProductPic())
                    .setProductName(item.getProductName())
                    .setProductSkuCode(item.getProductSkuCode())
                    .setProductCategoryId(item.getProductCategoryId())
                    .setProductSn(item.getProductSn())
                    .setProductAttr(item.getProductAttr())
                    .setCreateTime(DateUtils.getNowDate());
            orderItemList.add(orderItem);
        });

        // 保存订单商品明细表信息
        orderItemService.saveBatch(orderItemList);


        // 保存订单收货信息
        OrderAddress orderAddress = new OrderAddress()
                .setOrderId(order.getId())
                .setReceiverName(orderAddressQo.getReceiverName())
                .setReceiverPhone(orderAddressQo.getReceiverPhone())
                .setProvince(orderAddressQo.getProvinceLabel())
                .setCity(orderAddressQo.getCityLabel())
                .setRegion(orderAddressQo.getRegionLabel())
                .setAddress(orderAddressQo.getAddress())
                .setOrderNo(orderNo)
                .setCreateTime(DateUtils.getNowDate());
        orderAddressMapper.insert(orderAddress);

        // 加入延迟队列
        redisDelayQueueUtil.addDelayQueue(order.getOrderNo(), 30, TimeUnit.MINUTES, RedisDelayQueueEnum.MALL_ORDER_PAYMENT_TIMEOUT.getCode());
    }


    private String getOrderNo(Long storeId) {
        Long userId = SecurityUtils.getCurWechatLoginUserId();
        String subUserId = userId.toString().substring(userId.toString().length() - 6);
        // 下单时间戳
        String curTimeStr = DateUtils.dateTimeNow();

        // 2位随机字母 + 4位门店id+6位用户后缀id + 时间戳（12位）
        return RandomStringUtils.random(2, true, false).toUpperCase()
                + String.format("%04d", storeId) + subUserId + curTimeStr;
    }

    private void lockSkuStock(List<ConfirmOrder> confirmOrderList) throws CommonException {
        List<SkuStockQo> skuStockQoList = Lists.newArrayList();
        for (ConfirmOrder confirmOrder : confirmOrderList) {
            if (CollectionUtils.isEmpty(confirmOrder.getConfirmOrderItems())) {
                continue;
            }
            for (ConfirmOrderItem confirmOrderItem : confirmOrder.getConfirmOrderItems()) {
                skuStockQoList.add(new SkuStockQo()
                        .setProductSkuId(confirmOrderItem.getProductSkuId())
                        .setLockQuantity(confirmOrderItem.getQuantity()));
            }
        }
        if (CollectionUtils.isEmpty(skuStockQoList)) {
            throw new CommonException("商品为空，无法提交订单");
        }
        skuStockService.lockSkuStock(skuStockQoList);
    }
}
