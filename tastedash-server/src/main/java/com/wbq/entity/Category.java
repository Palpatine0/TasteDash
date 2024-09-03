package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("t_category")
public class Category {

    private Integer id;

    private String label;

    private String value;

    private Integer count;

    private Integer sele_quantity;

    private String cid; //  id标识

    @TableField(select = false)
    private List<Dish> dishList; // 拥有的菜品
}

