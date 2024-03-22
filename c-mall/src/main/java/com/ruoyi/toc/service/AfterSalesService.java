package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.AfterSalesRemarkLog;
import com.ruoyi.tob.qo.AfterSalesQo;
import com.ruoyi.tob.vo.AfterSalesVo;
import com.ruoyi.toc.entity.AfterSales;

import java.util.List;

public interface AfterSalesService {
    void applyAfterSales(AfterSales afterSales);

    Page<AfterSales> myAfterSalesList(BaseQo<AfterSales> baseQo);

    List<AfterSalesVo> queryAfterSalesList(AfterSalesQo afterSalesQo);

    AfterSalesVo queryAfterSalesDetail(Long id) throws CommonException;

    void dealAfterSales(AfterSalesQo afterSalesQo) throws CommonException;

    void addAfterSalesRemark(AfterSalesRemarkLog afterSalesRemarkLog) throws CommonException;
}
