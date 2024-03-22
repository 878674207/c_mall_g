package com.ruoyi.toc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.toc.entity.Basket;
import com.ruoyi.toc.qo.BasketQo;
import com.ruoyi.toc.vo.BasketVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BasketMapper extends BaseMapper<Basket> {
    List<BasketVo> queryBasketList(@Param("basketQo") BasketQo basketQo);
}
