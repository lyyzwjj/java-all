package com.wjjzst.applications.app001;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MmpThreadPool {

    private MmpThreadPool() {
        List<ArrayBlockingQueue<Request>> queues = RequestQueue.getInstance().getQueues();
        int threadPoolSize = 10;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolSize);
        for (int i = 0; i < threadPoolSize; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            queues.add(queue);
            threadPool.submit(new MmpThread(queue));
        }
    }

    private static class Singleton {
        private static final MmpThreadPool instance;

        static {
            instance = new MmpThreadPool();
        }

        public static MmpThreadPool getInstance() {
            return instance;
        }
    }

    public static MmpThreadPool getInstance() {
        return Singleton.getInstance();
    }

    public static void init() {
        getInstance();
    }
}
