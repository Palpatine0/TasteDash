package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.OrderDetail;
import com.wbq.mapper.OrderDetailMapper;
import com.wbq.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("orderDetailService")
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

}
