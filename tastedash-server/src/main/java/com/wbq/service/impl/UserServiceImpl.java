package com.wbq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.R;
import com.wbq.entity.User;
import com.wbq.mapper.UserMapper;
import com.wbq.service.IUserService;
import com.wbq.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserInfo(String code) {
        // Create a new User object
        User user = new User();

        // Retrieve the openid and session_key from the WeChat API response
        JSONObject object = WeChatUtil.getOpenId(code);
        System.out.println("object object object object object object ");
        System.out.println(object);
        String openid = object.get("openid").toString();
        String sessionkey = object.get("session_key").toString();
        String unionId = "";
        if (object.get("unionid") != null) {
            unionId = object.get("unionid").toString();
        }

        // Set the values to the user object
        user.setOpenid(openid);
        user.setSession_key(sessionkey);
        user.setUnionid(unionId);

        // Check if the openid exists in the database
        User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));

        if (existingUser != null) {
            // Update the existing user's session_key and union_id
            existingUser.setSession_key(sessionkey);
            existingUser.setUnionid(unionId);
            userMapper.updateById(existingUser);
        } else {
            // Insert the new user into the database
            userMapper.insert(user);
        }

        return user;
    }
}
