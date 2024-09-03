package com.wbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbq.entity.Table;

import java.util.List;
import java.util.Map;


public interface TableMapper extends BaseMapper<Table> {

    public List<Table> list(Map<String, Object> map);

    public Long getTotal(Map<String, Object> map);

}
