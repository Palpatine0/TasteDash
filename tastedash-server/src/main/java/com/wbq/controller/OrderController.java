package com.wbq.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.Order;
import com.wbq.entity.OrderDetail;
import com.wbq.entity.R;
import com.wbq.entity.User;
import com.wbq.service.IOrderDetailService;
import com.wbq.service.IOrderService;
import com.wbq.service.IUserService;
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

    @Autowired
    private IUserService userService;


    @RequestMapping("/saveOrder")
    public R saveOrder(@RequestBody Order order) {
        order.setCreateTime(new Date());
        orderService.save(order);
        for (OrderDetail od : order.getGoods_list()) {
            od.setMId(order.getId());
            orderDetailService.save(od);
        }
        return R.ok();
    }

    @RequestMapping("/getOrder")
    public R getOrder(@RequestBody Map<String, String> dto) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("table_number", dto.get("table_number"));
        orderWrapper.eq("transac_status", dto.get("transac_status"));
        orderWrapper.orderByDesc("create_time");
        Order order = orderService.list(orderWrapper).get(0);

        QueryWrapper<OrderDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("mId", order.getId());
        List<OrderDetail> goods_list = orderDetailService.list(detailQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("goods_list", goods_list);
        map.put("menu", order);
        return R.ok(map);
    }

    @RequestMapping("/getOrderListByUid")
    public R getOrderListByUid(@RequestBody Map<String, String> dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", dto.get("id"));
        List<Order> orderList = orderService.getOrderList(map);
        Long total = orderService.getTotal(map);

        for (Order order : orderList) {
            User user = userService.getUserInfo(order.getUid().toString());
            if (user != null) {
                order.setNickname(user.getNickname());
            }
            QueryWrapper<OrderDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("mId", order.getId());
            List<OrderDetail> goods_list = orderDetailService.list(detailQueryWrapper);
            order.setGoods_list(goods_list);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderList", orderList);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }


}
