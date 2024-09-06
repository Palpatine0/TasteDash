package com.wbq.controller;

import com.wbq.entity.User;
import com.wbq.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wbq.entity.R;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("saveUserAuthInfo")
    public R saveUserAuthInfo(@RequestBody Map<String, String> dto) {
        String code = dto.get("code");
        User user = userService.saveUserAuthInfo(code);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", user);
        return R.ok(map);
    }

    @PostMapping("saveUserInfo")
    public void saveUserInfo(@RequestBody Map<String, String> dto) {
        String openid = dto.get("openid");
        String nickname = dto.get("nickname");
        String avatar = dto.get("avatar");
        userService.saveUserInfo(openid, nickname, avatar);
    }

    @PostMapping("getUserInfo")
    public User getUserInfo(@RequestBody Map<String, String> dto) {
        String id = dto.get("id");
        return userService.getUserInfo(id);
    }


}
