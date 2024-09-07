package com.wbq.controller.admin;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.*;
import com.wbq.service.IOrderDetailService;
import com.wbq.service.IOrderService;
import com.wbq.service.IUserService;
import com.wbq.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IUserService userService;

    @RequestMapping("/getOrderList")
    public R getOrderList(@RequestBody Page page) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(page.getQuery())) {
            map.put("payment_status", page.getQuery().trim());
        }
        map.put("start", page.getStart());
        map.put("pageSize", page.getPageSize());
        List<Order> orderList = orderService.getOrderList(map);
        Long total = orderService.getTotal(map);

        for (Order order : orderList) {
            User user = userService.getUserInfo(order.getUid().toString());
            if (user != null) {
                order.setNickname(user.getNickname());
            }
        }

        // Add the modified list to the result map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderList", orderList);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }

    @PostMapping("/updateHandlingStatus")
    public R updateHandlingStatus(@RequestBody Order order) {
        Order resultOrder = orderService.getById(order.getId());
        if (order.getHandlingStatus() == 0) {
            resultOrder.setHandlingStatus(1);
        } else {
            resultOrder.setHandlingStatus(0);
        }
        orderService.updateById(resultOrder);
        return R.ok();
    }

    @GetMapping("/delete/{id}")
    public R delete(@PathVariable(value = "id") Integer id) {
        orderDetailService.remove(new QueryWrapper<OrderDetail>().eq("mId", id));
        orderService.removeById(id);
        return R.ok();
    }

    @GetMapping("/salesvolume")
    public R salesvolume() {
        List<SalesVolume> salesVolumeList = orderService.getSalesVolume();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("salesVolumeList", salesVolumeList);
        return R.ok(resultMap);
    }

}
