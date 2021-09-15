package com.wjjzst.applications.app001;


import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        MmpThreadPool.init();
        Integer productId = 1;
        for (int j = 0; j < 1000; j++) {
            for (int i = 0; i < 2; i++) {
                new Thread(() -> {
                    try {
                        Request request = new CacheRefreshRequest(productId, false);
                        RequestAsyncProcessService.process(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            if ((int) (Math.random() * 1000) > 100) {
                new Thread(() -> {
                    try {
                        Request request = new DBUpdateRequest(productId, 200);
                        RequestAsyncProcessService.process(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            for (int i = 0; i < 2; i++) {
                new Thread(() -> {
                    try {
                        Request request = new CacheRefreshRequest(productId, false);
                        RequestAsyncProcessService.process(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            TimeUnit.SECONDS.sleep(2);

        }
    }
}
