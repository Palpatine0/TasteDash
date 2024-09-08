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
        for (OrderDetail orderDetail : order.getGoods_list()) {
            orderDetail.setMId(order.getId());
            orderDetailService.save(orderDetail);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("oid", order.getId());
        return R.ok(map);
    }

    @RequestMapping("/getOrder")
    public R getOrder(@RequestBody Map<String, String> dto) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("id", dto.get("oid"));
        Order order = orderService.list(orderWrapper).get(0);

        QueryWrapper<OrderDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("mId", order.getId());
        List<OrderDetail> goods_list = orderDetailService.list(detailQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("dishList", goods_list);
        map.put("order", order);
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

    @RequestMapping("/updateOrderPaymentStatus")
    public void updateOrderPaymentStatus(@RequestBody Map<String, String> dto) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("id", dto.get("oid"));
        Order order = orderService.getOne(orderWrapper);
        order.setPaymentStatus(1);
        orderService.updateById(order);
    }

    @RequestMapping("/payWithWeChat")
    public R pay(@RequestBody Map<String, String> dto) {
        if (true) {
            return R.ok("支付成功");
        } else {
            return R.error("支付失败");
        }
    }

    @RequestMapping("/payWithBalance")
    public R payWithBalance(@RequestBody Map<String, String> dto) {
        // Fetch the order by ID
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("id", dto.get("oid"));
        Order order = orderService.getOne(orderWrapper);

        // Fetch the user by ID
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("id", dto.get("uid"));
        User user = userService.getOne(userWrapper);

        if (order == null) {
            return R.error("订单不存在");
        }

        if (user == null) {
            return R.error("用户不存在");
        }

        // Get the user's current balance and the total order amount
        int balance = user.getBalance();
        Double price = order.getSett_amount();

        // Check if the user has enough balance to pay for the order
        if (balance >= price) {
            // Deduct the order price from the user's balance
            user.setBalance(balance - price.intValue());
            userService.updateById(user);

            // Update the order payment status to 'paid' (assuming 1 means 'paid')
            order.setPaymentStatus(1);
            orderService.updateById(order);

            return R.ok("支付成功");
        } else {
            return R.error("余额不足，支付失败");
        }
    }

}
