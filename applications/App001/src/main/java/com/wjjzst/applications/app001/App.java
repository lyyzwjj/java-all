package com.wjjzst.applications.app001;


import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        MmpThreadPool.init();
        Integer productId = 1;
        for (int j = 0; j < 1000; j++) {
            for (int i = 0; i < 10; i++) {
                Request request = new CacheRefreshRequest(productId, false);
                RequestAsyncProcessService.process(request);
            }
            if ((int) (Math.random() * 1000) > 100) {
                Request request = new DBUpdateRequest(productId, true, 200);
                RequestAsyncProcessService.process(request);
            }
            for (int i = 0; i < 5; i++) {
                Request request = new CacheRefreshRequest(productId, false);
                RequestAsyncProcessService.process(request);
            }
            TimeUnit.MICROSECONDS.sleep(500);

        }
    }
}
