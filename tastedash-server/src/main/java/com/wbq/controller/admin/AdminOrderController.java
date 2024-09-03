package com.wbq.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.*;
import com.wbq.service.IOrderDetailService;
import com.wbq.service.IOrderService;
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

    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        System.out.println(pageBean);
        Map<String, Object> map = new HashMap<>();
        map.put("transac_status", pageBean.getQuery().trim());
        map.put("start", pageBean.getStart());
        map.put("pageSize", pageBean.getPageSize());
        List<Order> list = orderService.list(map);
        Long total = orderService.getTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderList", list);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }

    @PostMapping("/receiving")
    public R receiving(@RequestBody Order order) {
        Order resultOrder = orderService.getById(order.getId());
        resultOrder.setOrder_receiving(order.getOrder_receiving());
        orderService.updateById(resultOrder);
        return R.ok();
    }

    @PostMapping("/checkout")
    public R checkout(@RequestBody Order order) {
        Order resultOrder = orderService.getById(order.getId());
        resultOrder.setTransac_status(order.getTransac_status());
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
