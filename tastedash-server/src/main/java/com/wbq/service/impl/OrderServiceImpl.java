package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.Order;
import com.wbq.entity.SalesVolume;
import com.wbq.mapper.OrderMapper;
import com.wbq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> getOrderList(Map<String, Object> map) {
        return orderMapper.getOrderList(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return orderMapper.getTotal(map);
    }

    @Override
    public List<SalesVolume> getSalesVolume() {
        return orderMapper.getSalesVolume();
    }
}
