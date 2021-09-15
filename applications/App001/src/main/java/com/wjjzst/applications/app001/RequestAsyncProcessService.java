package com.wjjzst.applications.app001;

import java.util.concurrent.ArrayBlockingQueue;

public class RequestAsyncProcessService {
    public static void process(Request request) throws InterruptedException {
        Integer productId = request.getProductId();

        int h;
        int hash = (productId == null) ? 0 : (h = productId.toString().hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去
        RequestQueue requestQueue = RequestQueue.getInstance();
        int index = (requestQueue.getQueueSize() - 1) & hash;
        ArrayBlockingQueue<Request> queue = requestQueue.getQueueByIndex(index);
        if (request instanceof DBUpdateRequest) {
            System.out.println("添加更新数据库队列" + index);
        } else {
            System.out.println("添加更新缓存队列" + index);
        }
        queue.put(request);
    }
}
