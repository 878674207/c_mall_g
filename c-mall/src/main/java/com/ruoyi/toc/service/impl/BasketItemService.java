package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.toc.entity.BasketItem;
import com.ruoyi.toc.mapper.BasketItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BasketItemService extends ServiceImpl<BasketItemMapper, BasketItem> {
}
