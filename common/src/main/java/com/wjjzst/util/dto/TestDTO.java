package com.wjjzst.util.dto;

import com.wjjzst.util.enums.ComplexEnum;
import com.wjjzst.util.enums.TestEnum;
import lombok.Data;

/**
 * @author: Wjj
 * @create: 2020/9/11 7:08 下午
 * @Description
 */
@Data
public class TestDTO {
    private String name;
    private TestEnum testEnum;
    private ComplexEnum complexEnum;
}
