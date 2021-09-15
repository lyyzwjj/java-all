package com.wjjzst.applications.app001.blockQueue;

import java.util.concurrent.ArrayBlockingQueue;

public class Consumer {
    ArrayBlockingQueue<Integer> arrayBlockingQueue;

    public Consumer(ArrayBlockingQueue<Integer> arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    public void process() throws InterruptedException {
        while (true) {
            Integer take = arrayBlockingQueue.take();
            System.out.println("消费成功: " + take);
        }
    }
}
