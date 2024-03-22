package com.ruoyi.toc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tob.qo.AfterSalesQo;
import com.ruoyi.tob.vo.AfterSalesVo;
import com.ruoyi.toc.entity.AfterSales;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AfterSalesMapper extends BaseMapper<AfterSales> {

    List<AfterSalesVo> queryAfterSalesList(@Param("afterSalesQo") AfterSalesQo afterSalesQo);

}
