package com.ruoyi.toc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tob.qo.AfterSalesQo;
import com.ruoyi.toc.entity.AfterSalesItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AfterSalesItemMapper extends BaseMapper<AfterSalesItem> {

    List<Long> queryDistinctAfterSalesIds(@Param("afterSalesQo") AfterSalesQo afterSalesQo);
}
