package com.wbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbq.entity.Category;
import com.wbq.entity.Table;

import java.util.List;
import java.util.Map;


public interface CategoryMapper extends BaseMapper<Category> {

    List<Table> list(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);

    Category findById(Integer id);
}
