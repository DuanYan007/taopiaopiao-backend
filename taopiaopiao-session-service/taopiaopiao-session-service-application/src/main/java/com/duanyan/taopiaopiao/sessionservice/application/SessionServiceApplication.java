package com.duanyan.taopiaopiao.sessionservice.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 场次服务启动类
 *
 * @author duanyan
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "com.duanyan.taopiaopiao.sessionservice.application",
    "com.duanyan.taopiaopiao.common"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
    "com.duanyan.taopiaopiao.sessionservice.application",
    "com.duanyan.taopiaopiao.common"
})
@MapperScan("com.duanyan.taopiaopiao.sessionservice.application.mapper")
public class SessionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionServiceApplication.class, args);
    }
}
