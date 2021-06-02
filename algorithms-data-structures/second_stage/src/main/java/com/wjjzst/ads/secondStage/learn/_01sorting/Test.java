package com.wjjzst.ads.secondStage.learn._01sorting;

import com.alibaba.fastjson.JSON;

/**
 * @Author: Wjj
 * @Date: 2020/4/21 11:30 下午
 * @desc: 数组是值传递而非引用传递
 */
public class Test {
    public static void main(String[] args) {
        Integer[] array = {2, 3, 4};
        changeArray(array);
        System.out.println(array);
    }

    static void changeArray(Integer[] array) {
        Integer[] newArray = {1, 3, 5};
        array = newArray;
    }


}