package com.wjjzst.common.utils;

import lombok.AllArgsConstructor;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Wjj
 * @Date: 2021/6/3 12:45 下午
 * @desc:
 */
public class FileSize {
    public static void main(String[] args) {
        // String dir = "/Users/wjj/IdeaProjects/java-all/algorithms-data-structures/first-stage";
        String dir = "/Users/wjj/IdeaProjects/wjjjavalearn/algorithms-data-structures/first_stage";
        File file = new File(dir);
        Counter counter =new Counter(1);
        countFileSize(file,counter);
        System.out.println(counter.count);
    }

    public static void countFileSize(File file, Counter counter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles(pathname -> {
                Set<String> types = new HashSet<>(
                        Arrays.asList(".idea", ".iml", ".DS_Store","target","xml")
                );
                String name = pathname.getName();
                for (String type : types) {
                    if (name.endsWith(type)) {
                        return false;
                    }
                }
                return true;
            });
            for (File f : files) {
                countFileSize(f, counter);

            }
        } else {
            counter.count += 1;
        }
    }

    @AllArgsConstructor
    static class Counter {
        int count;
    }

}
