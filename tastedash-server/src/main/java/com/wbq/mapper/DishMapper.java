package com.wbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbq.entity.Dish;

import java.util.List;
import java.util.Map;

public interface DishMapper extends BaseMapper<Dish> {

    List<Dish> list(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);

}
