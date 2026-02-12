package com.duanyan.taopiaopiao.sessionservice.application.config;

import com.duanyan.taopiaopiao.common.response.Result;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
@Configuration
public class FeignResultWrapper {

    @Bean
    public Decoder resultDecoder() {
        return (response, type) -> {
            // 直接使用 ResponseEntity 或处理 Result 包装
            // 这里简化处理，让 Feign 直接返回 Result，在业务层提取 data
            return new Decoder.Default().decode(response, type);
        };
    }
}