package com.wjjzst.applications.app001.blockQueue;

import java.util.concurrent.ArrayBlockingQueue;

public class Producer {
    ArrayBlockingQueue<Integer> arrayBlockingQueue;

    public Producer(ArrayBlockingQueue<Integer> arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    public void process() throws InterruptedException {
        while (true) {
            int put = (int) (Math.random() * 100);
            arrayBlockingQueue.put(put);
            System.out.println("生成成功: " + put);
        }
    }
}
