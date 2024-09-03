package com.wbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.Admin;
import com.wbq.mapper.AdminMapper;
import com.wbq.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("adminService")
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Integer update(Admin admin) {
        return adminMapper.update(admin);
    }
}
