package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.Table;

import java.util.List;
import java.util.Map;


public interface ITableService extends IService<Table> {

    public List<Table> list(Map<String, Object> map);

    public Long getTotal(Map<String, Object> map);


}
