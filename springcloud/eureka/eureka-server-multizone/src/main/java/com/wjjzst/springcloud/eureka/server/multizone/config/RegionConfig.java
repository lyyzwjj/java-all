package com.wjjzst.springcloud.eureka.server.multizone.config;

import com.netflix.discovery.EurekaClientConfig;
import com.netflix.eureka.EurekaServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Author: Wjj
 * @Date: 2020/4/27 1:36 上午
 * @desc:
 */
@Configuration
@AutoConfigureBefore(EurekaServerAutoConfiguration.class)
public class RegionConfig {
    @Bean
    @ConditionalOnMissingBean
    public EurekaServerConfig eurekaServerConfig(EurekaClientConfig clientConfig){
        EurekaServerConfigBean server = new EurekaServerConfigBean();
        if(clientConfig.shouldRegisterWithEureka()){
            // Set a sensible default if we are supposed to replicate
            server.setRegistrySyncRetries(5);
        }
        server.setRemoteRegionAppWhitelist(new HashMap<>());
        return server;
    }
}
