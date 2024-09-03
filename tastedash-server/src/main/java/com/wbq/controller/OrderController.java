package com.wbq.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.Order;
import com.wbq.entity.OrderDetail;
import com.wbq.entity.R;
import com.wbq.service.IOrderDetailService;
import com.wbq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;


    @RequestMapping("/saveOrder")
    public R create(@RequestBody Order order) {
        order.setOrder_time(new Date());
        orderService.save(order);
        for (OrderDetail od : order.getGoods_list()) {
            od.setMId(order.getId());
            orderDetailService.save(od);
        }
        return R.ok();
    }

    @RequestMapping("/get")
    public R get(String table_number, String transac_status) {
        Order order = orderService.list(new QueryWrapper<Order>().eq("table_number", table_number).eq("transac_status", transac_status).orderByDesc("order_time")).get(0);
        List<OrderDetail> goods_list = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("mId", order.getId()));
        Map<String, Object> map = new HashMap<>();
        map.put("goods_list", goods_list);
        map.put("menu", order);
        return R.ok(map);
    }


}
