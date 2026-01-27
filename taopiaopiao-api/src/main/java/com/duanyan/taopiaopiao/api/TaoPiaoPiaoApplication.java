package com.duanyan.taopiaopiao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 淘泡泡后端应用启动类
 *
 * @author duanyan
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.duanyan.taopiaopiao")
public class TaoPiaoPiaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoPiaoPiaoApplication.class, args);
        System.out.println("""

                =====================================================
                   淘泡泡后端启动成功!
                   访问地址: http://localhost:8080
                   API文档: http://localhost:8080/doc.html
                =====================================================
                """);
    }
}
