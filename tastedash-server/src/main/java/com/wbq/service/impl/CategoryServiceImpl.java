package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.Category;
import com.wbq.entity.Table;
import com.wbq.mapper.CategoryMapper;
import com.wbq.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 菜单分类Service实现类
 * @author
 * @site
 * @company 南通小锋网络科技有限公司
 * @create 2021-11-23 9:08
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Table> list(Map<String, Object> map) {
        return categoryMapper.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return categoryMapper.getTotal(map);
    }
}
