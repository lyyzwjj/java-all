package com.wjjzst.springcloud.eureka.server.single;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author: Wjj
 * @Date: 2020/4/24 12:28 上午
 * @desc:
 */
@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class EurekaServerSingleApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerSingleApplication.class, args);
    }
}
