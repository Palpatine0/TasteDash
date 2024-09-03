package com.wbq.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wbq.constant.SystemConstant;
import com.wbq.entity.Admin;
import com.wbq.entity.R;
import com.wbq.service.IAdminService;
import com.wbq.util.JwtUtils;
import com.wbq.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/admin/user/")
public class AdminUserController {

    @Autowired
    private IAdminService adminService;

    private final static Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @PostMapping("login")
    public R adminLogin(@RequestBody Admin admin) {
        if (admin == null) {
            return R.error();
        }
        if (StringUtil.isEmpty(admin.getUserName())) {
            return R.error("用户名不能为空！");
        }
        if (StringUtil.isEmpty(admin.getPassword())) {
            return R.error("密码不能为空！");
        }
        Admin resultAdmin = adminService.getOne(new QueryWrapper<Admin>().eq("userName", admin.getUserName()));
        if (resultAdmin == null) {
            return R.error("用户名不存在！");
        }
        if (!resultAdmin.getPassword().trim().equals(admin.getPassword())) {
            return R.error("用户名或者密码错误！");
        }
        String token = JwtUtils.createJWT("-1", "admin", SystemConstant.JWT_TTL);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("token", token);
        resultMap.put("resultAdmin", resultAdmin);
        return R.ok(resultMap);
    }

    @PostMapping("updatePassword")
    public R modifyPassword(@RequestBody Admin admin) {
        if (StringUtil.isEmpty(admin.getUserName())) {
            return R.error("用户名不能为空！");
        }
        if (StringUtil.isEmpty(admin.getNewPassword())) {
            return R.error("新密码不能为空！");
        }
        adminService.update(admin);
        return R.ok();
    }


}
