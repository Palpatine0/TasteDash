package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.Dish;

import java.util.List;
import java.util.Map;


public interface IDishService extends IService<Dish> {

    public List<Dish> list(Map<String,Object> map);

    public Long getTotal(Map<String,Object> map);

}
