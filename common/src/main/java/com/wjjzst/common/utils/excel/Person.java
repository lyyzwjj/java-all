package com.wjjzst.common.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Wjj
 * @Date: 2021/6/10 3:06 下午
 * @desc:
 */
@Data
@AllArgsConstructor
public class Person {
    @ExcelProperty("id")
    private Long id;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private Integer age;
    // @ExcelProperty(value = "生日", converter = DateConverter.class)
    @ExcelProperty("生日")
    private Date birthDay;
}
