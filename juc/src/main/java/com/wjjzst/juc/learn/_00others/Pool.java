package com.wjjzst.juc.learn._00others;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Obj {
    public Obj(Integer i) {
        this.i = i;
    }

    private Integer i;
}

/**
 * 结论  一定要保证 多线程内每个线程 runnable 干的活比较多 把list的for循环放在runnable里面做  才能最大化利用多线程
 */
public class Pool {
    public static void hello(Obj i) {
        // System.out.println(i);
    }

    public static void main(String[] args) {
        // main1();
        // main2();
        // main3();
        // main4();
        main5();
    }

    private static void main2() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CountDownLatch cdl = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            new EachTableThread(cdl, executorService).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
    public static void main5() {
        CountDownLatch cdl = new CountDownLatch(2);
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                try {
                    for (int m = 0; m < 20000; m++) {
                        CountDownLatch countDownLatch = new CountDownLatch(20000000);
                        // System.out.println("new countDownLatch" + countDownLatch);
                        for (int k = 0; k < 20000000; k++) {
                            final int j = k;
                            executorService.execute(() -> {
                                try {
                                    for (int l = 0; l < 1000000; l++) {
                                        hello(new Obj(j));
                                    }
                                } finally {
                                    // System.out.println("use countDownLatch" + countDownLatch);
                                    countDownLatch.countDown();
                                }
                            });
                        }
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    executorService.shutdown();
                    cdl.countDown();
                }
            }).start();
        }
        try {
            cdl.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CountDownLatch cdl = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                try {
                    for (int m = 0; m < 20000; m++) {
                        CountDownLatch countDownLatch = new CountDownLatch(20000);
                        // System.out.println("new countDownLatch" + countDownLatch);
                        for (int k = 0; k < 20000; k++) {
                            final int j = k;
                            executorService.execute(() -> {
                                try {
                                    hello(new Obj(j));
                                } finally {
                                    // System.out.println("use countDownLatch" + countDownLatch);
                                    countDownLatch.countDown();
                                }
                            });
                        }
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    cdl.countDown();
                }
            }).start();
        }
        try {
            cdl.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void main4() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        System.out.println("new countDownLatch" + countDownLatch);
        for (int m = 0; m < 2; m++) {
            executorService.execute(new InnerRunnable(new Obj(m), countDownLatch));
            //                    executorService.execute(() -> {
            //                        try {
            //                            hello(new Obj(j));
            //                        } finally {
            //                            System.out.println("use countDownLatch" + countDownLatch);
            //                            countDownLatch.countDown();
            //                        }
            //                    });
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void main3() {
        CountDownLatch cdl = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                try {
                    ExecutorService executorService = Executors.newFixedThreadPool(8);
                    CountDownLatch countDownLatch = new CountDownLatch(2);
                    System.out.println("new countDownLatch" + countDownLatch);
                    for (int m = 0; m < 2; m++) {
                        executorService.execute(new InnerRunnable(new Obj(m), countDownLatch));
                        //                    executorService.execute(() -> {
                        //                        try {
                        //                            hello(new Obj(j));
                        //                        } finally {
                        //                            System.out.println("use countDownLatch" + countDownLatch);
                        //                            countDownLatch.countDown();
                        //                        }
                        //                    });
                    }
                    try {
                        countDownLatch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    executorService.shutdown();
                } finally {
                    cdl.countDown();
                }
            }).start();
        }
        try {
            cdl.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class EachTableThread extends Thread {
    public EachTableThread(CountDownLatch cdl, ExecutorService executorService) {
        this.cdl = cdl;
        this.executorService = executorService;
        this.setName("eachTableName");
    }

    private final CountDownLatch cdl;
    private final ExecutorService executorService;

    @Override
    public void run() {
        CountDownLatch innerCdl = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            new InnerThread(innerCdl, executorService).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cdl.countDown();
    }
}

class InnerRunnable implements Runnable {
    Obj obj;
    CountDownLatch countDownLatch;

    public InnerRunnable(Obj obj, CountDownLatch countDownLatch) {
        this.obj = obj;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        countDownLatch.countDown();
        // System.out.println(obj);
    }
}

class InnerThread extends Thread {
    public InnerThread(CountDownLatch innerCdl, ExecutorService executorService) {
        this.innerCdl = innerCdl;
        this.executorService = executorService;
        this.setName("innerName");
    }

    private final CountDownLatch innerCdl;
    private final ExecutorService executorService;

    @Override
    public void run() {
        try {
            //for (int m = 0; m < 20000; m++) {
            CountDownLatch countDownLatch = new CountDownLatch(2);
            //System.out.println("new countDownLatch" + countDownLatch);
            for (int k = 0; k < 2; k++) {
                final int j = k;
                executorService.execute(() -> {
                    try {
                        Pool.hello(new Obj(j));
                    } finally {
                        //System.out.println("use countDownLatch" + countDownLatch);
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
            //}
        } catch (InterruptedException e) {
            System.out.println("youwenti");
            e.printStackTrace();
        } finally {
            innerCdl.countDown();
        }
        innerCdl.countDown();
    }
}