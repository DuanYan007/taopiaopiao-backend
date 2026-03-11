package com.duanyan.taopiaopiao.seatternplateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 座位模板服务启动类
 *
 * @author duanyan
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.duanyan.taopiaopiao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.duanyan.taopiaopiao")
public class SeatTemplateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatTemplateServiceApplication.class, args);
    }
}
