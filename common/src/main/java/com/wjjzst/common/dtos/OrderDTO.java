package com.wjjzst.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Data  //会复写equals 和hashCode方法
@ToString
public class OrderDTO implements Serializable {
    private Integer id;
    private String name;

    public OrderDTO() {
        this.id = (int) (Math.random() * Integer.MAX_VALUE);
        this.name = UUID.randomUUID().toString();
    }
}


