package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.Admin;


public interface IAdminService extends IService<Admin> {

    public Integer update(Admin admin);

}
