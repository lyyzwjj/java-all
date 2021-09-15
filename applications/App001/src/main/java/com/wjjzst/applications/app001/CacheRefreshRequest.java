package com.wjjzst.applications.app001;

public class CacheRefreshRequest implements Request {
    private Integer productId;
    private boolean forceRefresh;

    public CacheRefreshRequest(Integer productId, boolean forceRefresh) {
        this.productId = productId;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        System.out.println("更新缓存");
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return forceRefresh;
    }
}
