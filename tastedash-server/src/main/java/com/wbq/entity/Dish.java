package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_dish")
public class Dish {

    private Integer id;

    private String name;

    private String image;

    private Integer typeId;

    @TableField(select = false)
    private Category type;

    private Integer monthlysale;

    private Float unitprice;

    private String unit;

    private Integer quantity;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private Date time;

    private boolean onsale;


}
