package com.duanyan.taopiaopiao.userservice.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务启动类
 *
 * @author duanyan
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "com.duanyan.taopiaopiao.userservice.application",
    "com.duanyan.taopiaopiao.common"
})
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
