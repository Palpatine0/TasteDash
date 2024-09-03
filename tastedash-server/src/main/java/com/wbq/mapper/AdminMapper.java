package com.wbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wbq.entity.Admin;

public interface AdminMapper extends BaseMapper<Admin> {

    public Integer update(Admin admin);

}
