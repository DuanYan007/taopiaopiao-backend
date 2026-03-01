package com.duanyan.taopiaopiao.seckillservice.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
        "com.duanyan.taopiaopiao.seckillservice.application",
        "com.duanyan.taopiaopiao.common"
})
@EnableDiscoveryClient
@MapperScan("com.duanyan.taopiaopiao.seckillservice.application.mapper")
public class SeckillServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillServiceApplication.class, args);
    }
}
