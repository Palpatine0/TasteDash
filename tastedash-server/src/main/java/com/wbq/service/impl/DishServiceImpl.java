package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.Dish;
import com.wbq.mapper.DishMapper;
import com.wbq.service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("dishService")
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Dish> list(Map<String, Object> map) {
        return dishMapper.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return dishMapper.getTotal(map);
    }
}
