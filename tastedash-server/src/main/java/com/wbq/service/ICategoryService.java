package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.Category;
import com.wbq.entity.Table;

import java.util.List;
import java.util.Map;


public interface ICategoryService extends IService<Category> {

    public List<Table> list(Map<String, Object> map);

    public Long getTotal(Map<String, Object> map);


}
