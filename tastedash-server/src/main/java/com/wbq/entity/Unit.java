package com.wbq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_unit")
public class Unit {

    private Integer id;

    private String label;

    private String value;

}
