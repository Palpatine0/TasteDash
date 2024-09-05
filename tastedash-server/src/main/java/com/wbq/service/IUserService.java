package com.wbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wbq.entity.User;

public interface IUserService extends IService<User> {
    User saveUserAuthInfo(String code);

    void saveUserInfo(String openid, String nickname, String avatar);
}
