package com.wjjzst.util.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.wjjzst.util.file.SourceFileUtil;

import java.io.*;
import java.util.List;

/**
 * @Author: Wjj
 * @Date: 2021/7/9 10:25 上午
 * @desc:
 */
public class CsvDemo {
    public static void main(String[] args) throws IOException, CsvException {
        // InputStream fileFromResources = SourceFileUtil.getFileFromResources("/cvs/mmp_batch_right.csv");
        InputStream resourceAsStream = SourceFileUtil.class.getClassLoader().getResourceAsStream("/cvs/mmp_batch_right.csv");
            System.out.println(resourceAsStream);
        //File file = new File("e:\\read.csv");
        // FileReader fReader = new FileReader(fileFromResources);
//        CSVReader csvReader = new CSVReader(fReader);
//        String[] strs = csvReader.readNext();
//        if(strs != null && strs.length > 0){
//            for(String str : strs)
//                if(null != str && !str.equals(""))
//                    System.out.print(str + " , ");
//            System.out.println("\n---------------");
//        }
//        List<String[]> list = csvReader.readAll();
//        for(String[] ss : list){
//            for(String s : ss)
//                if(null != s && !s.equals(""))
//                    System.out.print(s + " , ");
//            System.out.println();
//        }
//        csvReader.close();
    }
}
