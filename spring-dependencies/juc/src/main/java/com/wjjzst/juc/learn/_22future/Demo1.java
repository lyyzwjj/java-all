package com.wjjzst.juc.learn._22future;

public class Demo1 {
    public static void main(String[] args) {
        ProductFactory pf = new ProductFactory();
        //下单,交钱
        Future f = pf.createProduct("蛋糕");
        System.out.println("我去上班了,下了班我来拿取蛋糕");
        //拿着订单获取产品
        System.out.println("我拿着蛋糕回家" + f.get());
    }
}
