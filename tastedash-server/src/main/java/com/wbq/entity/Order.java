package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_order")
public class Order {

    private Integer id;

    private String order_no;

    private String table_number;

    private String number_of_diners;

    private Double sett_amount;

    private String transac_status;

    private String order_receiving;

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date order_time;

    @TableField(select = false,exist = false)
    private List<OrderDetail> goods_list;

}
