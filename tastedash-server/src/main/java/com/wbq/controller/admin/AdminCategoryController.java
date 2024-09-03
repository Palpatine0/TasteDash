package com.wbq.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.*;
import com.wbq.service.ICategoryService;
import com.wbq.service.IDishService;
import com.wbq.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IDishService dishService;

    @RequestMapping("/listAll")
    public R listAll() {
        List<Category> list = categoryService.list();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList", list);
        return R.ok(resultMap);
    }

    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        System.out.println(pageBean);
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("pageSize", pageBean.getPageSize());
        List<Table> list = categoryService.list(map);
        Long total = categoryService.getTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList", list);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }

    @PostMapping("/add")
    public R add(@RequestBody Category category) {
        category.setValue(category.getLabel());
        category.setCid("a" + DateUtil.getCurrentDateStr());
        category.setCount(0);
        category.setSele_quantity(0);
        categoryService.save(category);
        return R.ok();
    }

    @GetMapping("/delete")
    public R delete(Integer id) {
        if (dishService.list(new QueryWrapper<Dish>().eq("typeId", id)).size() > 0) {
            return R.error("该菜品类目下有菜品，不能删除！");
        } else {
            categoryService.removeById(id);
            return R.ok();
        }
    }

}
