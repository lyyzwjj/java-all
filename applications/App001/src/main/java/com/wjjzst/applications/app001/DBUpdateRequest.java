package com.wjjzst.applications.app001;

public class DBUpdateRequest implements Request {
    private Integer productId;
    private boolean forceRefresh;
    private Integer size;

    public DBUpdateRequest(Integer productId, Integer size) {
        this.productId = productId;
        this.size = size;
    }

    @Override
    public void process() {
        System.out.println("更新数据库" + size);
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
