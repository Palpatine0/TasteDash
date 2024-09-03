package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {
    private Integer id;

    private String openid;

    private String session_key;

    private String username;

    private String avatar;

    private String unionid;

    private Integer credit;

    private Timestamp add_time;
}
