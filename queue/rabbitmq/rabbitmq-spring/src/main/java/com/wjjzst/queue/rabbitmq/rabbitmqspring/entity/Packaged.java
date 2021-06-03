package com.wjjzst.queue.rabbitmq.rabbitmqspring.entity;

/**
 * @Author: Wjj
 * @Date: 2021/6/3 11:56 上午
 * @desc:
 */
public class Packaged {

    private String id;

    private String name;

    private String description;

    public Packaged() {
    }

    public Packaged(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

