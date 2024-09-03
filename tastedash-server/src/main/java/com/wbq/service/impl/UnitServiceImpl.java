package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.Unit;
import com.wbq.mapper.UnitMapper;
import com.wbq.service.IUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("unitService")
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {

    @Autowired
    private UnitMapper unitMapper;

}
