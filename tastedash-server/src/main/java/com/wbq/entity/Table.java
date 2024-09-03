package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_table")
public class Table implements Serializable {

    private Integer id;

    private String number;

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date time;

    private String qrcode;


}
