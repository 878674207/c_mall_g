package com.ruoyi.tob.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.RedisConstants;
import com.ruoyi.common.constant.RoleConstants;
import com.ruoyi.common.core.domain.ApproveRequest;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ImageBusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.CusImage;
import com.ruoyi.system.service.CusImageService;
import com.ruoyi.tob.converter.ProductConverter;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.entity.ProductAttributeValue;
import com.ruoyi.tob.entity.SkuStock;
import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.tob.mapper.ProductMapper;
import com.ruoyi.tob.mapper.StoreMapper;
import com.ruoyi.tob.qo.ProductQo;
import com.ruoyi.tob.service.ProductService;
import com.ruoyi.tob.vo.ProductVo;
import com.ruoyi.toc.entity.ProductCollection;
import com.ruoyi.toc.entity.StoreAttention;
import com.ruoyi.toc.mapper.ProductCollectionMapper;
import com.ruoyi.toc.mapper.StoreAttentionMapper;
import com.ruoyi.toc.qo.ClientProductQo;
import com.ruoyi.toc.qo.SettleQo;
import com.ruoyi.toc.service.CustomerDeliveryAddressService;
import com.ruoyi.toc.service.StoreClientService;
import com.ruoyi.toc.service.impl.CustomerDeliveryAddressServiceImpl;
import com.ruoyi.toc.vo.ConfirmOrder;
import com.ruoyi.toc.vo.ConfirmOrderItem;
import com.ruoyi.toc.vo.ConfirmOrderVo;
import com.ruoyi.toc.vo.DeliveryVo;
import com.ruoyi.toc.vo.PaymentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>  implements ProductService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private CusImageService cusImageService;

    @Autowired
    private SkuStockServiceImpl skuStockService;

    @Autowired
    private ProductAttributeValueServiceImpl productAttributeValueService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CustomerDeliveryAddressServiceImpl customerDeliveryAddressService;

    @Autowired
    private StoreAttentionMapper storeAttentionMapper;


    @Autowired
    private ProductCollectionMapper productCollectionMapper;

    @Autowired
    private StoreClientService storeClientService;


    @Override
    public void saveProductInfo(ProductQo productQo) {
        if (Objects.isNull(productQo.getId())) {
            addProductInfo(productQo);
        } else {
            updateProductInfo(productQo);
        }
    }

    @Override
    public Page queryProductList(ProductQo productQo) {
        Page page = productQo.initPage();
        page(page, new QueryWrapper<Product>()
                .select(Product.class, item -> !item.getProperty().equals("detailHtml"))
                .like(StringUtils.isNotEmpty(productQo.getProductName()), "product_name", productQo.getProductName())
                .like(StringUtils.isNotEmpty(productQo.getProductSn()), "product_sn", productQo.getProductSn())
                .eq(Objects.nonNull(productQo.getProductCategoryId()), "product_category_id", productQo.getProductCategoryId())
                .eq(Objects.nonNull(productQo.getPublishStatus()), "publish_status", productQo.getPublishStatus())
                .eq(StringUtils.isNotEmpty(productQo.getApproveStatus()), "approve_status", productQo.getApproveStatus())
                .eq(RoleConstants.MALL_STORE_MANAGER.equals(SecurityUtils.getLoginRoleCode()), "store_id", SecurityUtils.getStoreId())
                .orderByDesc("create_time"));
        page.setRecords(getResultVoList(page.getRecords()));
        return page;
    }

    private List<ProductVo> getResultVoList(List<Product> records) {
        if (CollectionUtils.isEmpty(records)) {
            return Lists.newArrayList();
        }
        List<ProductVo> productVoList = ProductConverter.INSTANCE.toProductVoList(records);
        productVoList.forEach(item -> item.setApproveFlag(RoleConstants.MALL_MANAGER.equals(SecurityUtils.getLoginRoleCode()) && Constants.PENDING_APPROVAL.equals(item.getApproveStatus())));
        return productVoList;
    }

    @Override
    public String generateSn() {
        String storeIdStr = getStoreIdStr(SecurityUtils.getStoreId());
        String sn;
        List<Product> products;
        do {
            sn = storeIdStr + RandomStringUtils.random(10, true, true).toUpperCase();
            products = list(new QueryWrapper<Product>().eq("product_sn", sn).last("limit 1"));
        } while (CollectionUtils.isNotEmpty(products));
        return sn;
    }

    @Override
    public ProductVo queryProductDetail(Long id) throws CommonException {
        Product product = getById(id);
        if (Objects.isNull(product)) {
            throw new CommonException("该商品不存在");
        }
        ProductVo productVo = ProductConverter.INSTANCE.toProductVo(product);

        // 轮播图
        productVo.setCarouselImages(cusImageService.list(new QueryWrapper<CusImage>()
                .eq("business_type", ImageBusinessType.PRODUCT_CAROUSEL_IMAGE.getCode())
                .eq("business_id", id)
                .eq("del_flag", "0")));

        // sku库存
        productVo.setSkuStockList(skuStockService.list(new QueryWrapper<SkuStock>().eq("product_id", id)));

        // 商品参数值
        productVo.setProductAttributeValueList(productAttributeValueService.list(new QueryWrapper<ProductAttributeValue>().eq("product_id", id)));

        return productVo;
    }

    @Override
    public void batchOperate(ProductQo productQo) throws CommonException {

        List<Long> productIdlist = productQo.getIdlist();

        if (CollectionUtils.isEmpty(productIdlist)) {
            return;
        }
        if (Constants.ONE.equals(productQo.getOperateType())) {

            //校验锁定库存
            checkProductLockStock(productIdlist);

            // 批量删除
            batchDelete(productIdlist);
        } else if (Constants.TWO.equals(productQo.getOperateType())) {

            //校验锁定库存
            checkProductLockStock(productIdlist);

            // 批量下架
            Product update = new Product().setPublishStatus(0);
            update(update, new QueryWrapper<Product>().in("id", productIdlist));
        } else if (Constants.THREE.equals(productQo.getOperateType())) {

            // 批量下架
            Product update = new Product().setPublishStatus(1);
            update(update, new QueryWrapper<Product>().in("id", productIdlist));
        }
    }

    /**
     *  如果该商品下的sku存在锁定库存，即未结束的订单，此商品不允许下架或者删除
     * @param productIdlist
     */
    private void checkProductLockStock(List<Long> productIdlist) throws CommonException {
        List<SkuStock> stockList = skuStockService.list(new QueryWrapper<SkuStock>().in("product_id", productIdlist));
        Set<Long> productIds = stockList.stream().filter(item -> item.getLockStock() > 0).map(SkuStock::getProductId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(productIds)) {
            return;
        }
        List<Product> productList = list(new LambdaQueryWrapper<Product>().in(Product::getId, productIds));
        if (CollectionUtils.isEmpty(productList)) {
            return;
        }

        String productName = productList.stream().map(Product::getProductName).collect(Collectors.joining(","));
        throw new CommonException("商品" + productName + "存在未完成的订单，无法下架或者删除！");

    }

    @Override
    public void productApprove(ApproveRequest approveRequest) throws CommonException {
        if (!RoleConstants.MALL_MANAGER.equals(SecurityUtils.getLoginRoleCode())) {
            throw new CommonException("该角色无权限审核");
        }

        List<Product> products = list(new QueryWrapper<Product>().in("id", approveRequest.getIds()));
        if (products.size() != approveRequest.getIds().size()) {
            throw new CommonException("部分商品不存在");
        }
        List<Product> tempList = products.stream().filter(item -> !Constants.PENDING_APPROVAL.equals(item.getApproveStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(tempList)) {
            throw new CommonException("部分商品已审核，请勿重复操作");
        }
        Product update = new Product();
        update.setApproveStatus(approveRequest.getApprovalResults());
        update.setUpdateTime(DateUtils.getNowDate());
        update.setApprovalComment(approveRequest.getApprovalComment());
        update(update, new QueryWrapper<Product>().in("id", approveRequest.getIds()));
    }

    @Override
    public Page queryClientProductList(ClientProductQo clientProductQo) {
        Page page = clientProductQo.initPage();

        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>()
                .select(Product.class, item -> !item.getProperty().equals("detailHtml"))
                .eq("publish_status", 1)
                .eq("approve_status", "approved")
                .eq(Objects.nonNull(clientProductQo.getProductCategoryId()), "product_category_id", clientProductQo.getProductCategoryId())
                .ne(Objects.nonNull(clientProductQo.getExStoreId()), "store_id", clientProductQo.getExStoreId())
                .eq(Objects.nonNull(clientProductQo.getStoreId()), "store_id", clientProductQo.getStoreId())
                .like(StringUtils.isNotBlank(clientProductQo.getKeyword()), "product_name", clientProductQo.getKeyword());
        if (Objects.isNull(clientProductQo.getSort())) {
            page(page, queryWrapper);
            return page;
        }

        if (clientProductQo.getSort().intValue() == 2 ) {
            queryWrapper.orderByDesc("sale");
        } else if (clientProductQo.getSort().intValue() == 3 ) {
            queryWrapper.orderByAsc("price");
        } else if (clientProductQo.getSort().intValue() == 4 ) {
            queryWrapper.orderByDesc("price");
        }
        page(page, queryWrapper);
        return page;
    }

    @Override
    public ProductVo queryClientProductDetail(Long id) throws CommonException {
        ProductVo productVo = queryProductDetail(id);

        // 店铺信息
        buildStoreInfoVo(productVo);

        // 店铺推荐
        productVo.setStoreRecommend(list(new QueryWrapper<Product>()
                        .select("id, product_category_id, product_name, price, pic")
                .eq("store_id", productVo.getStoreId())
                .eq("publish_status", 1)
                .eq("approve_status", "approved")
                .orderByDesc("sale")
                .last("limit 3")));

        // 商品浏览量
        buildVisitCount(productVo);

        // 商品当前用户收藏情况
        buildProductCollection(productVo);

        // 店铺当前用户关注情况
        buildStoreAttention(productVo);

        // 物流信息
        buildDeliveryInfo(productVo);

        // 封装前端组件所需的商品属性格式
        buildProductAttribute(productVo);

        return productVo;
    }

    private void buildDeliveryInfo(ProductVo productVo) {
        DeliveryVo deliveryVo = new DeliveryVo();
        deliveryVo.setDeliveryFrom(Objects.nonNull(productVo.getStoreInfoVo()) ? productVo.getStoreInfoVo().getEntAddress() : Strings.EMPTY);
        deliveryVo.setDeliveryTo(customerDeliveryAddressService.queryMyDefaultAddress());
        productVo.setDeliveryVo(deliveryVo);
    }

    private void buildProductAttribute(ProductVo productVo) {
        List<SkuStock> skuStockList = productVo.getSkuStockList();
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        List<JSONArray> collect = skuStockList.stream().map(item -> JSON.parseArray(item.getSpData()))
                .collect(Collectors.toList());

        // 取出商品属性分类（如颜色。内存）
        List<String> attributeList = new ArrayList<>();
        for (int i = 0; i < collect.get(0).size(); i++) {
            JSONObject jsonObject = collect.get(0).getJSONObject(i);
            attributeList.add ((String) jsonObject.get("key"));
        }

        // 取出每个属性分类不重复的value值
        List<Set> setList = new ArrayList<>();
        for (int i = 0;i < attributeList.size(); i++) {
            setList.add(new HashSet());
        }

        for(int i = 0;i < setList.size(); i++) {
            Set set = setList.get(i);
            for (int j = 0; j< collect.size();j++) {
                set.add(collect.get(j).getJSONObject(i).get("value"));
            }
        }

        // 封装返回格式
        List<JSONObject> result = new ArrayList<>();
        for (int i = 0; i < attributeList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", attributeList.get(i));
            jsonObject.put("list", setList.get(i).stream().map(item -> {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", item);
                return jsonObject1;
            }).collect(Collectors.toList()));

            result.add(jsonObject);
        }
        productVo.setProductAttribute(result);

    }

    private void buildStoreInfoVo(ProductVo productVo) {
        productVo.setStoreInfoVo(storeClientService.queryClientStoreDetail(productVo.getStoreId()));
    }

    private void buildVisitCount(ProductVo productVo) {
        // 从缓存取游览量
        String KEY = RedisConstants.PRODUCT_VISIT_COUNT_PRE + "storeId:" +  productVo.getStoreId() + "productId:" + productVo.getId();
        Number visitCount = redisCache.getCacheObjectV2(KEY);
        if (Objects.nonNull(visitCount)) {
            productVo.setVisitCount(visitCount.longValue());
        } else {
            redisCache.setCacheObjectV2(KEY, productVo.getVisitCount());
        }

        // 游览量自增
        redisCache.incr(KEY, 1);
    }

    private void buildProductCollection(ProductVo productVo) {
        productVo.setProductCollection(productCollectionMapper.selectOne(new LambdaQueryWrapper<ProductCollection>()
                .eq(ProductCollection::getProductId, productVo.getId())
                .eq(ProductCollection::getCustomerId, SecurityUtils.getCurWechatLoginUserId())));
    }

    private void buildStoreAttention(ProductVo productVo) {
        productVo.setStoreAttention(storeAttentionMapper.selectOne(new LambdaQueryWrapper<StoreAttention>()
                .eq(StoreAttention::getStoreId, productVo.getStoreId())
                .eq(StoreAttention::getCustomerId, SecurityUtils.getCurWechatLoginUserId())));
    }

    @Override
    public ConfirmOrderVo productSettle(SettleQo settleQo) throws CommonException {
        ConfirmOrderVo confirmOrderVo = new ConfirmOrderVo();

        if (Objects.isNull(settleQo.getDeliveryAddressId())) {
            // 默认收获地址
            confirmOrderVo.setOrderAddress(customerDeliveryAddressService.queryMyDefaultAddress());
        } else {
            confirmOrderVo.setOrderAddress(customerDeliveryAddressService.getById(settleQo.getDeliveryAddressId()));
        }

        // 校验商品
        Product product = getById(settleQo.getProductId());
        SkuStock skuStock = skuStockService.getById(settleQo.getProductSkuId());
        if (Objects.isNull(product) || Objects.isNull(skuStock)) {
            throw new CommonException("商品不存在");
        }

        // 构建确认单
        BigDecimal productTotalPrice = skuStock.getPrice().multiply(BigDecimal.valueOf(settleQo.getQuantity()));
        ConfirmOrder confirmOrder = new ConfirmOrder().setStoreId(product.getStoreId()).setStoreName(product.getStoreName());
        ConfirmOrderItem confirmOrderItem = new ConfirmOrderItem()
                .setProductId(product.getId())
                .setProductSkuId(skuStock.getId())
                .setQuantity(settleQo.getQuantity())
                .setPrice(skuStock.getPrice())
                .setTotalPrice(productTotalPrice)
                .setProductPic(product.getPic())
                .setProductName(product.getProductName())
                .setProductSubTitle(product.getSubTitle())
                .setProductSkuCode(skuStock.getSkuCode())
                .setProductCategoryId(product.getProductCategoryId())
                .setProductSn(product.getProductSn())
                .setProductAttr(skuStock.getSpData());

        confirmOrder.setConfirmOrderItems(Arrays.asList(confirmOrderItem));
        confirmOrderVo.setConfirmOrderList(Arrays.asList(confirmOrder));

        // 支付信息 其中运费暂时写死为0
        PaymentVo paymentVo = new PaymentVo();
        paymentVo.setDeliverPrice(BigDecimal.ZERO);
        paymentVo.setProductTotalPrice(productTotalPrice);
        paymentVo.setTotalPrice(paymentVo.getProductTotalPrice().add(paymentVo.getDeliverPrice()));
        confirmOrderVo.setPaymentVo(paymentVo);

        return confirmOrderVo;
    }

    private void batchDelete(List<Long> idlist) {
        if (CollectionUtils.isEmpty(idlist)) {
            return;
        }

        // 删除轮播图
        CusImage update = new CusImage();
        update.setDelFlag(1);
        cusImageService.update(update, new QueryWrapper<CusImage>()
                .eq("business_type", ImageBusinessType.PRODUCT_CAROUSEL_IMAGE.getCode())
                .in("business_id", idlist));

        // 删除sku库存
        skuStockService.remove(new QueryWrapper<SkuStock>().in("product_id", idlist));


        // 删除商品属性参数值
        productAttributeValueService.remove(new QueryWrapper<ProductAttributeValue>().in("product_id", idlist));

        // 删除商品
        remove(new QueryWrapper<Product>().in("id", idlist));
    }

    private String getStoreIdStr(Long storeId) {
        if (Objects.isNull(storeId)) {
            return RandomStringUtils.random(4, true, true).toUpperCase();
        }
        String storeIdStr = storeId.toString();
        if (storeIdStr.length() == 1) {
            return "000" + storeIdStr;
        } else if (storeIdStr.length() == 2) {
            return "00" + storeIdStr;
        } else if (storeIdStr.length() == 3) {
            return "0" + storeIdStr;
        } else {
            return storeIdStr;
        }
    }

    private void addProductInfo(ProductQo productQo) {
        Product product = ProductConverter.INSTANCE.toProductEntity(productQo);

        Long storeId = SecurityUtils.getStoreId();
        if (Objects.nonNull(storeId)) {
            StoreInfo storeInfo = storeMapper.selectById(storeId);
            product.setStoreId(storeId);
            product.setStoreName(Objects.nonNull(storeInfo) ? storeInfo.getStoreName() : StringUtils.EMPTY);
        }

        product.setCreateTime(DateUtils.getNowDate())
                .setPublishStatus(0)
                .setNewStatus(0)
                .setUpdateTime(DateUtils.getNowDate())
                .setApproveStatus(Constants.PENDING_APPROVAL);
        if (CollectionUtils.isNotEmpty(productQo.getSkuStockList())) {
            product.setStock(productQo.getSkuStockList().stream().mapToLong(SkuStock::getStock).sum());
        }
        save(product);

        // 保存轮播图
        if (CollectionUtils.isNotEmpty(productQo.getCarouselImages())) {
            cusImageService.batchSaveByType(productQo.getCarouselImages(), ImageBusinessType.PRODUCT_CAROUSEL_IMAGE.getCode(),
                    product.getId());
        }

        // 处理sku code
        buildSkuStockCode(productQo);
        // 保存sku库存
        List<SkuStock> skuStockList = productQo.getSkuStockList();
        if (CollectionUtils.isNotEmpty(skuStockList)) {
            skuStockList.forEach(item -> item.setProductId(product.getId()));
            skuStockService.saveBatch(skuStockList);
        }

        // 保存商品参数值
        List<ProductAttributeValue> productAttributeValueList = productQo.getProductAttributeValueList();
        if (CollectionUtils.isNotEmpty(productAttributeValueList)) {
            productAttributeValueList.forEach(item -> item.setProductId(product.getId()));
            productAttributeValueService.saveBatch(productAttributeValueList);
        }

    }

    private void buildSkuStockCode(ProductQo productQo) {
        List<SkuStock> skuStockList = productQo.getSkuStockList();
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        for (int i = 0; i < skuStockList.size(); i++) {
            SkuStock skuStock = skuStockList.get(i);
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                StringBuilder sb = new StringBuilder();
                sb.append(productQo.getProductSn());
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }

            if ("[]".equals(skuStock.getSpData())) {
                skuStock.setSpData("[{\"key\":\"默认属性\",\"value\":\"默认属性\"}]");
            }
        }
    }

    private void updateProductInfo(ProductQo productQo) {
        Product product = ProductConverter.INSTANCE.toProductEntity(productQo);
        product.setUpdateTime(DateUtils.getNowDate()).setApproveStatus(Constants.PENDING_APPROVAL);
        if (CollectionUtils.isNotEmpty(productQo.getSkuStockList())) {
            product.setStock(productQo.getSkuStockList().stream().mapToLong(SkuStock::getStock).sum());
        }
        updateById(product);

        // 修改轮播图
        if (CollectionUtils.isNotEmpty(productQo.getCarouselImages())) {
            cusImageService.batchUpdate(productQo.getCarouselImages(), ImageBusinessType.PRODUCT_CAROUSEL_IMAGE.getCode(),
                    product.getId());
        }

        // 处理sku code
        buildSkuStockCode(productQo);
        // 修改sku库存
        if (CollectionUtils.isNotEmpty(productQo.getSkuStockList())) {
            skuStockService.remove(new QueryWrapper<SkuStock>().eq("product_id", productQo.getId()));
            productQo.getSkuStockList().forEach(item -> item.setProductId(product.getId()));
            skuStockService.saveBatch(productQo.getSkuStockList());
        }

        // 修改商品参数值
        List<ProductAttributeValue> productAttributeValueList = productQo.getProductAttributeValueList();
        if (CollectionUtils.isNotEmpty(productAttributeValueList)) {
            productAttributeValueService.remove(new QueryWrapper<ProductAttributeValue>().eq("product_id", productQo.getId()));
            productAttributeValueList.forEach(item -> item.setProductId(product.getId()));
            productAttributeValueService.saveBatch(productAttributeValueList);
        }
    }
}
