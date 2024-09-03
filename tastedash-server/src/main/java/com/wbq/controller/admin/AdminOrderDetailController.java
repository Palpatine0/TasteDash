package com.wbq.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.OrderDetail;
import com.wbq.entity.R;
import com.wbq.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin/orderDetail")
public class AdminOrderDetailController {

    @Autowired
    private IOrderDetailService orderDetailService;

    @GetMapping("/list")
    public R listByOrderId(Integer id) {
        System.out.println("id=" + id);
        List<OrderDetail> orderDetailList = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("mId", id));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", orderDetailList);
        return R.ok(resultMap);
    }

}
