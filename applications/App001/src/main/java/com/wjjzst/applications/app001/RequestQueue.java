package com.wjjzst.applications.app001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RequestQueue {

    private final List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();
    private Map<Integer, Boolean> flagMap = new ConcurrentHashMap<Integer, Boolean>();

    public List<ArrayBlockingQueue<Request>> getQueues() {
        return queues;
    }

    private static class Singleton {
        private static final RequestQueue instance;

        static {
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance() {
            return instance;
        }
    }

    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    public int getQueueSize() {
        return queues.size();
    }

    public ArrayBlockingQueue<Request> getQueueByIndex(int index) {
        return queues.get(index);
    }

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }
}
