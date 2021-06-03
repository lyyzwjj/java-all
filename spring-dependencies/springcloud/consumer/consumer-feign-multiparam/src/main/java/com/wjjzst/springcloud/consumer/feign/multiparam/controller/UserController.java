package com.wjjzst.springcloud.consumer.feign.multiparam.controller;

import com.wjjzst.common.dto.TestDTO;
import com.wjjzst.common.enums.ComplexEnum;
import com.wjjzst.common.enums.TestEnum;
import com.wjjzst.springcloud.consumer.feign.multiparam.model.User;
import com.wjjzst.springcloud.consumer.feign.multiparam.service.UserFeignService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Wjj
 * @Date: 2020/5/5 10:34 上午
 * @desc:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserFeignService userFeignService;

    /**
     * 用于演示Feign的Get请求多参数传递
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(@RequestBody @ApiParam(name = "用户", value = "传入json格式", required = true) User user) {
        return userFeignService.addUser(user);
    }

    /**
     * 用于演示Feign的Post请求多参数传递
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateUser(@RequestBody @ApiParam(name = "用户", value = "传入json格式", required = true) User user) {
        return userFeignService.upadateUser(user);
    }

    @RequestMapping(value = "/testEnum",method= RequestMethod.POST)
    public String testEnum(@RequestBody TestDTO dto) {
//    @RequestMapping(value = "/testEnum",method= RequestMethod.GET)
//    public String testEnum() {
//        TestDTO dto = new TestDTO();
//        dto.setName("haha");
//        dto.setTestEnum(TestEnum.HELLO);
//        dto.setComplexEnum(ComplexEnum.COMPLEX_ENUM_A);
        return userFeignService.testEnum(dto);
    }
}
