package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.OrderDetail;
import com.wbq.entity.R;
import com.wbq.entity.User;

public interface IUserService extends IService<User> {
    public User getUserInfo(String code);
}
