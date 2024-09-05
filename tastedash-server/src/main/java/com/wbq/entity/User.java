package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String openid;

    private String session_key;

    private String nickname;

    private String avatar;

    private String unionid;

    private Integer credit;

    @TableField("create_time")
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date createTime;

    private int deleted;


    @TableField("delete_time")
    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date deleteTime;
}
