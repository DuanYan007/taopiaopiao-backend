package com.duanyan.taopiaopiao.eventservice.application.controller;

import com.duanyan.taopiaopiao.common.oss.OssUtil;
import com.duanyan.taopiaopiao.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin/files")
@RequiredArgsConstructor
@Tag(name = "文件管理", description = "文件上传接口")
public class FileController {

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 图片访问URL
     */
    @PostMapping("/upload")
    @Operation(summary = "上传图片", description = "支持jpg、png、gif、webp格式，最大5MB")
    public Result<Map<String, String>> uploadImage(
            @Parameter(description = "图片文件", required = true)
            @RequestParam("file") MultipartFile file) {

        log.info("开始上传文件: {}, size: {}", file.getOriginalFilename(), file.getSize());

        try {
            // 上传到OSS
            String fileUrl = OssUtil.uploadFile(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", file.getOriginalFilename());

            log.info("文件上传成功: {}", fileUrl);
            return Result.success(result);

        } catch (IllegalArgumentException e) {
            log.error("文件参数校验失败: {}", e.getMessage());
            return Result.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return Result.fail(500, "文件上传失败: " + e.getMessage());
        }
    }
}
