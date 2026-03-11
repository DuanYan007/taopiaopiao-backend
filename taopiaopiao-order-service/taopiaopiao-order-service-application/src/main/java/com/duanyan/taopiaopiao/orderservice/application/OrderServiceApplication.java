package com.duanyan.taopiaopiao.orderservice.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.duanyan.taopiaopiao.orderservice.application",
        "com.duanyan.taopiaopiao.common"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "com.duanyan.taopiaopiao.orderservice.application",
        "com.duanyan.taopiaopiao.common"
})
@EnableScheduling
@MapperScan("com.duanyan.taopiaopiao.orderservice.application.mapper")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
