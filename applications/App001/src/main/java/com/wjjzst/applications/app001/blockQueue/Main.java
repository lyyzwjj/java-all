package com.wjjzst.applications.app001.blockQueue;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(100);
        new Thread(() -> {
            try {
                new Producer(arrayBlockingQueue).process();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                new Consumer(arrayBlockingQueue).process();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
