package com.wbq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wbq.entity.User;
import com.wbq.mapper.UserMapper;
import com.wbq.service.IUserService;
import com.wbq.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveUserAuthInfo(String code) {
        User user = new User();

        JSONObject object = WeChatUtil.getOpenId(code);
        String openid = object.get("openid").toString();
        String sessionkey = object.get("session_key").toString();
        String unionId = "";
        String id = "";
        if (object.get("unionid") != null) {
            unionId = object.get("unionid").toString();
        }

        user.setOpenid(openid);
        user.setSession_key(sessionkey);
        user.setUnionid(unionId);
        user.setCreateTime(new Date());

        User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (existingUser != null) {
            existingUser.setSession_key(sessionkey);
            existingUser.setUnionid(unionId);
            userMapper.updateById(existingUser);
            user.setId(existingUser.getId());
        } else {
            userMapper.insert(user);
            user.setId(user.getId());
        }
        return user;
    }

    @Override
    public void saveUserInfo(String openid, String nickname, String avatar) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        User user = userMapper.selectOne(wrapper);

        if (user != null) {
            user.setNickname(nickname);
            user.setAvatar(avatar);
            userMapper.updateById(user);
        } else {
            throw new RuntimeException("User not found with openid: " + openid);
        }
    }

    @Override
    public User getUserInfo(String id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
    }


}
