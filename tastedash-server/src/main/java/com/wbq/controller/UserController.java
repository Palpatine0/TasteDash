package com.wbq.controller;

import com.wbq.entity.User;
import com.wbq.service.IOrderDetailService;
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

    @PostMapping("getUserInfo")
    public R getUserInfo(@RequestBody Map<String, String> codeMap) {
        String code = codeMap.get("code");
        User user = userService.getUserInfo(code);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", user);
        return R.ok(map);
    }

}
