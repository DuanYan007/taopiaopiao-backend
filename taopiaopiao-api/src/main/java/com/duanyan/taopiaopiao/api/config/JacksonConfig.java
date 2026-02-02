package com.duanyan.taopiaopiao.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 配置
 *
 * @author duanyan
 * @since 1.0.0
 */
@Configuration
public class JacksonConfig {

    /**
     * 配置 ObjectMapper，支持多种日期时间格式
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 注册 JavaTimeModule，支持 Java 8 日期时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 使用自定义的 LocalDateTime 反序列化器，支持多种格式
        javaTimeModule.addDeserializer(LocalDateTime.class, new MultiFormatLocalDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // LocalDate
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));

        // LocalTime
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));

        objectMapper.registerModule(javaTimeModule);

        // 禁用将日期写为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 忽略未知属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }

    /**
     * 支持多种 LocalDateTime 格式的反序列化器
     */
    private static class MultiFormatLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

        private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,  // ISO 8601 格式: 2026-01-31T19:45:00
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),  // 标准格式: 2026-01-31 19:45:00
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")  // 明确指定 T 分隔符
        };

        public MultiFormatLocalDateTimeDeserializer() {
            super(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        @Override
        public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser parser, com.fasterxml.jackson.databind.DeserializationContext context) throws java.io.IOException {
            String dateStr = parser.getValueAsString();

            // 尝试使用每种格式解析
            for (DateTimeFormatter formatter : FORMATTERS) {
                try {
                    return LocalDateTime.parse(dateStr, formatter);
                } catch (Exception ignored) {
                    // 继续尝试下一个格式
                }
            }

            // 所有格式都失败，抛出异常
            throw new IllegalArgumentException(
                "Cannot parse LocalDateTime: " + dateStr + ". Supported formats: " +
                "yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd HH:mm:ss"
            );
        }
    }
}
