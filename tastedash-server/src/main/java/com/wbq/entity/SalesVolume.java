package com.wbq.entity;

import lombok.Data;

@Data
public class SalesVolume {

    private String time;

    private Double sales_volume;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(Double sales_volume) {
        this.sales_volume = sales_volume;
    }
}
