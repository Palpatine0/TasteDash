package com.wbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbq.entity.Order;
import com.wbq.entity.SalesVolume;

import java.util.List;
import java.util.Map;


public interface OrderMapper extends BaseMapper<Order> {

    public List<Order> getOrderList(Map<String, Object> map);

    public Long getTotal(Map<String, Object> map);

    public List<SalesVolume> getSalesVolume();

}
