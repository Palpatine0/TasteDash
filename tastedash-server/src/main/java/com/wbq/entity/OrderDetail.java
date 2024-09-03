package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("t_order_detail")
public class OrderDetail {

    private Integer id;

    private Integer mId;

    private String name;

    private String quantity;

    private String unit;

    private String image;

    private Double total_price;

}
