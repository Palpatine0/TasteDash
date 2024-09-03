package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.Table;
import com.wbq.mapper.TableMapper;
import com.wbq.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("tableService")
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements ITableService {

    @Autowired
    private TableMapper tableMapper;

    @Override
    public List<Table> list(Map<String, Object> map) {
        return tableMapper.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return tableMapper.getTotal(map);
    }
}
