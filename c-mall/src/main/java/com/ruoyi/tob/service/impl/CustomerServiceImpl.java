package com.ruoyi.tob.service.impl;

import com.ruoyi.tob.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {



}
