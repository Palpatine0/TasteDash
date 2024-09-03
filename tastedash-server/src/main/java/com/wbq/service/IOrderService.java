package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.Order;
import com.wbq.entity.SalesVolume;

import java.util.List;
import java.util.Map;


public interface IOrderService extends IService<Order> {

    public List<Order> list(Map<String, Object> map);

    public Long getTotal(Map<String, Object> map);

    public List<SalesVolume> getSalesVolume();

}
