package com.wbq.controller;


import com.wbq.entity.Category;
import com.wbq.entity.R;
import com.wbq.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping("/listAll")
    public R listAll() {
        List<Category> list = categoryService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("categoryListAll", list);
        return R.ok(map);
    }

}
