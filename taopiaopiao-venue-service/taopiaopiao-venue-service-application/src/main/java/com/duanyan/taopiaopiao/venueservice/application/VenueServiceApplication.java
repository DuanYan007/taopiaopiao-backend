package com.duanyan.taopiaopiao.venueservice.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 场馆服务启动类
 *
 * @author duanyan
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "com.duanyan.taopiaopiao.venueservice.application",
    "com.duanyan.taopiaopiao.common"
})
@EnableDiscoveryClient
@MapperScan("com.duanyan.taopiaopiao.venueservice.application.mapper")
public class VenueServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenueServiceApplication.class, args);
    }
}
