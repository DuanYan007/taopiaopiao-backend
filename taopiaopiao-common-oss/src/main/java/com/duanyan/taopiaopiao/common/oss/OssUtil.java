package com.duanyan.taopiaopiao.common.oss;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 阿里云OSS文件上传工具类
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
public class OssUtil {

    /**
     * OSS endpoint
     */
    private static final String ENDPOINT = "https://oss-cn-beijing.aliyuncs.com";

    /**
     * OSS bucket名称
     */
    private static final String BUCKET_NAME = "taopiao";

    /**
     * OSS region
     */
    private static final String REGION = "cn-beijing";

    /**
     * 允许上传的图片格式
     */
    private static final String[] ALLOWED_IMAGE_TYPES = {
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    };

    /**
     * 最大文件大小 5MB
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 上传文件到OSS
     *
     * @param inputStream 文件流
     * @param filename    原始文件名
     * @param contentType 文件类型
     * @param fileSize    文件大小
     * @return 文件访问URL
     */
    public static String uploadFile(InputStream inputStream, String filename, String contentType, long fileSize) {
        return uploadFile(inputStream, filename, contentType, fileSize, null);
    }

    /**
     * 上传文件到OSS（指定目录）
     *
     * @param inputStream 文件流
     * @param filename    原始文件名
     * @param contentType 文件类型
     * @param fileSize    文件大小
     * @param customDir   自定义目录（可为空）
     * @return 文件访问URL
     */
    public static String uploadFile(InputStream inputStream, String filename, String contentType, long fileSize, String customDir) {
        // 参数校验
        if (inputStream == null) {
            throw new IllegalArgumentException("文件流不能为空");
        }
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 文件大小校验
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }

        // 文件类型校验
        if (!isAllowedImageType(contentType)) {
            throw new IllegalArgumentException("只支持上传jpg、png、gif、webp格式的图片");
        }

        // 生成文件名: UUID.扩展名
        String extension = getFileExtension(filename);
        String fileName = IdUtil.simpleUUID() + "." + extension;

        // 构建上传路径: customDir/fileName 或 images/fileName
        String objectName = (customDir != null && !customDir.isEmpty()
                ? customDir + "/"
                : "images/") + fileName;

        OSS ossClient = null;
        try {
            // 创建OSSClient（使用环境变量凭证）
            ossClient = new OSSClientBuilder().build(
                    ENDPOINT,
                    new EnvironmentVariableCredentialsProvider()
            );

            // 设置元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileSize);
            metadata.setContentType(contentType);

            // 上传文件
            ossClient.putObject(BUCKET_NAME, objectName, inputStream, metadata);

            // 返回文件访问URL
            String fileUrl = "https://" + BUCKET_NAME + "." + ENDPOINT.substring(8) + "/" + objectName;
            log.info("文件上传成功: {}", fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除OSS文件
     *
     * @param fileUrl 文件URL
     */
    public static void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        // 从URL中提取objectName
        // URL格式: https://taopiao.oss-cn-beijing.aliyuncs.com/images/xxx.jpg
        String objectName = extractObjectName(fileUrl);
        if (objectName == null) {
            log.warn("无法从URL中提取对象名称: {}", fileUrl);
            return;
        }

        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(
                    ENDPOINT,
                    new EnvironmentVariableCredentialsProvider()
            );

            ossClient.deleteObject(BUCKET_NAME, objectName);
            log.info("文件删除成功: {}", fileUrl);

        } catch (Exception e) {
            log.error("文件删除失败: {}, error: {}", fileUrl, e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 检查是否为允许的图片类型
     */
    private static boolean isAllowedImageType(String contentType) {
        if (contentType == null) {
            return false;
        }
        for (String type : ALLOWED_IMAGE_TYPES) {
            if (type.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "jpg";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "jpg";
    }

    /**
     * 从URL中提取objectName
     */
    private static String extractObjectName(String fileUrl) {
        try {
            // URL格式: https://taopiao.oss-cn-beijing.aliyuncs.com/images/xxx.jpg
            int index = fileUrl.indexOf(BUCKET_NAME + ".");
            if (index == -1) {
                return null;
            }
            // 跳过 bucket_name.endpoint/
            int start = index + BUCKET_NAME.length() + ENDPOINT.length() - 5; // -5 for "https"
            return fileUrl.substring(start);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取OSS客户端（用于特殊操作）
     */
    public static OSS getOssClient() {
        return new OSSClientBuilder().build(
                ENDPOINT,
                new EnvironmentVariableCredentialsProvider()
        );
    }

    /**
     * 获取Bucket名称
     */
    public static String getBucketName() {
        return BUCKET_NAME;
    }

    /**
     * 获取Endpoint
     */
    public static String getEndpoint() {
        return ENDPOINT;
    }
}
