package com.wjjzst.applications.app001;

public interface Request {

    void process();

    Integer getProductId();

    boolean isForceRefresh();

}
