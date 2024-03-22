package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.tob.converter.AfterSalesConverter;
import com.ruoyi.tob.entity.AfterSalesRemarkLog;
import com.ruoyi.tob.mapper.AfterSalesRemarkLogMapper;
import com.ruoyi.tob.qo.AfterSalesQo;
import com.ruoyi.tob.vo.AfterSalesVo;
import com.ruoyi.tob.vo.OrderVo;
import com.ruoyi.toc.entity.AfterSales;
import com.ruoyi.toc.entity.AfterSalesItem;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.entity.OrderAddress;
import com.ruoyi.toc.entity.OrderItem;
import com.ruoyi.toc.mapper.AfterSalesItemMapper;
import com.ruoyi.toc.mapper.AfterSalesMapper;
import com.ruoyi.toc.mapper.OrderAddressMapper;
import com.ruoyi.toc.mapper.OrderMapper;
import com.ruoyi.toc.service.AfterSalesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AfterSalesServiceImpl implements AfterSalesService {

    @Autowired
    private AfterSalesMapper afterSalesMapper;

    @Autowired
    private AfterSalesItemMapper afterSalesItemMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AfterSalesRemarkLogMapper afterSalesRemarkLogMapper;

    @Override
    public void applyAfterSales(AfterSales afterSalesEntity) {
        afterSalesEntity.setCustomerId(SecurityUtils.getCurWechatLoginUserId());
        afterSalesEntity.setCreateTime(DateUtils.getNowDate());
        afterSalesEntity.setAfterSalesStatus("apply");
        afterSalesEntity.setAfterSalesNo("SH" + afterSalesEntity.getOrderNo());
        afterSalesMapper.insert(afterSalesEntity);
        afterSalesEntity.getAfterSalesItemList().forEach(item -> {
            item.setAfterSalesId(afterSalesEntity.getId());
            item.setCreateTime(DateUtils.getNowDate());
            afterSalesItemMapper.insert(item);
        });
    }

    @Override
    public Page<AfterSales> myAfterSalesList(BaseQo<AfterSales> baseQo) {
        Page<AfterSales> page = afterSalesMapper.selectPage(baseQo.initPage(), new LambdaQueryWrapper<AfterSales>()
                .eq(AfterSales::getCustomerId, SecurityUtils.getCurWechatLoginUserId()));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }
        List<Long> afterSalesIds = page.getRecords().stream().map(AfterSales::getId).collect(Collectors.toList());
        List<AfterSalesItem> afterSalesItemList = afterSalesItemMapper.selectList(new LambdaQueryWrapper<AfterSalesItem>()
                .in(AfterSalesItem::getAfterSalesId, afterSalesIds));
        page.getRecords().forEach(item -> item.setAfterSalesItemList(afterSalesItemList.stream()
                .filter(subItem -> subItem.getAfterSalesId().compareTo(item.getId()) == 0).collect(Collectors.toList())));
        return page;
    }

    @Override
    public List<AfterSalesVo> queryAfterSalesList(AfterSalesQo afterSalesQo) {
        if (StringUtils.isNotBlank(afterSalesQo.getProductName()) || StringUtils.isNotBlank(afterSalesQo.getAfterSalesMethods())) {
            List<Long> afterSalesIds = afterSalesItemMapper.queryDistinctAfterSalesIds(afterSalesQo);
            if (CollectionUtils.isEmpty(afterSalesIds)) {
                return Lists.newArrayList();
            }
            afterSalesQo.setAfterSalesIds(afterSalesIds);
        }
        afterSalesQo.startPage();
        List<AfterSalesVo> afterSalesVos = afterSalesMapper.queryAfterSalesList(afterSalesQo);
        buildAfterSalesItem(afterSalesVos);
        return afterSalesVos;
    }

    @Override
    public AfterSalesVo queryAfterSalesDetail(Long id) throws CommonException {
        AfterSales afterSales = afterSalesMapper.selectById(id);
        if (Objects.isNull(afterSales)) {
            throw new CommonException("该笔售后不存在");
        }
        AfterSalesVo afterSalesVo = AfterSalesConverter.INSTANCE.toAfterSalesVo(afterSales);
        afterSalesVo.setOrderInfo(orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, afterSales.getOrderNo()).last("limit 1")));
        afterSalesVo.setAfterSalesItemList(afterSalesItemMapper.selectList(new LambdaQueryWrapper<AfterSalesItem>()
                .eq(AfterSalesItem::getAfterSalesId, id)));
        afterSalesVo.setOrderAddress(orderAddressMapper.selectOne(new LambdaQueryWrapper<OrderAddress>()
                .eq(OrderAddress::getOrderNo, afterSales.getOrderNo())
                .last("limit 1")));
        afterSalesVo.setAfterSalesRemarkLogList(afterSalesRemarkLogMapper.selectList(new LambdaQueryWrapper<AfterSalesRemarkLog>().eq(AfterSalesRemarkLog::getAfterSalesId, id)));
        return afterSalesVo;
    }

    @Override
    public void dealAfterSales(AfterSalesQo afterSalesQo) throws CommonException {
        AfterSales afterSales = afterSalesMapper.selectById(afterSalesQo.getId());
        if (Objects.isNull(afterSales)) {
            throw new CommonException("该笔售后不存在");
        }
        if (!"apply".equals(afterSales.getAfterSalesStatus())) {
            throw new CommonException("该笔售后已处理");
        }
        afterSalesMapper.updateById(new AfterSales().setId(afterSalesQo.getId()).setAfterSalesStatus(afterSalesQo.getAfterSalesStatus()));
    }

    @Override
    public void addAfterSalesRemark(AfterSalesRemarkLog afterSalesRemarkLog) throws CommonException {
        AfterSales afterSales = afterSalesMapper.selectById(afterSalesRemarkLog.getAfterSalesId());
        if (Objects.isNull(afterSales)) {
            throw new CommonException("该笔售后不存在");
        }
        afterSalesRemarkLog.setAfterSalesStatus(afterSales.getAfterSalesStatus());
        afterSalesRemarkLog.setCreateTime(DateUtils.getNowDate());
        afterSalesRemarkLog.setCreateBy(SecurityUtils.getUsername());
        afterSalesRemarkLogMapper.insert(afterSalesRemarkLog);
    }

    private void buildAfterSalesItem(List<AfterSalesVo> afterSalesVos) {
        if (CollectionUtils.isEmpty(afterSalesVos)) {
            return;
        }
        List<Long> afterSalesIds = afterSalesVos.stream().map(AfterSalesVo::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(afterSalesIds)) {
            return;
        }
        List<AfterSalesItem> afterSalesItemList = afterSalesItemMapper.selectList(new LambdaQueryWrapper<AfterSalesItem>().in(AfterSalesItem::getAfterSalesId, afterSalesIds));
        if (CollectionUtils.isEmpty(afterSalesItemList)) {
            return;
        }
        Map<Long, List<AfterSalesItem>> afterSalesItemMap = afterSalesItemList.stream().collect(Collectors.groupingBy(AfterSalesItem::getAfterSalesId));
        afterSalesVos.forEach(item -> item.setAfterSalesItemList(afterSalesItemMap.get(item.getId())));
    }

}
