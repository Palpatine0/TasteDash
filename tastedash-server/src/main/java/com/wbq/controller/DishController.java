package com.wbq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.entity.Category;
import com.wbq.entity.Dish;
import com.wbq.entity.R;
import com.wbq.service.ICategoryService;
import com.wbq.service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    @Autowired
    private ICategoryService categoryService;


    @RequestMapping("/listAll")
    public R listAll() {
        List<Category> categoryList = categoryService.list();
        for (Category category : categoryList) {
            List<Dish> dishList = dishService.list(new QueryWrapper<Dish>().eq("typeId", category.getId()));
            category.setDishList(dishList);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("allDish", categoryList);
        return R.ok(map);
    }

}
