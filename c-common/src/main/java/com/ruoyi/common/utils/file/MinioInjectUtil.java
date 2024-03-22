package com.ruoyi.common.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.config.MinioConfig;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;

import io.minio.*;
import io.minio.http.Method;
import sun.misc.BASE64Encoder;

/**
 * Minio 文件存储工具类 使用方式需要@autowire注入
 *
 * @author ruoyi
 */
@Component
public class MinioInjectUtil {

    private static MinioInjectUtil utils;

    @Resource
    public MinioClient minioClient;

    @PostConstruct
    public void init() {
        utils = this;
        utils.minioClient = this.minioClient;
    }

    /**
     * 上传文件
     *
     * @param bucketName
     *            桶名称
     * @param file
     *            文件
     * @throws IOException
     */
    public static String uploadFile(String bucketName, MultipartFile file) throws IOException {
        if (StringUtils.isBlank(bucketName)) {
            throw new RuntimeException("请填写需要上传的桶名称！");
        }
        if (file == null) {
            throw new RuntimeException("请上传文件！");
        }
        String url = "";
        String fileName = extractFileNameMi(file);
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectArgs args = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream, file.getSize(), -1).contentType(file.getContentType()).build();
            utils.minioClient.putObject(args);
            url = utils.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).method(Method.GET).build());
            url = url.substring(0, url.indexOf('?'));
            return ServletUtils.urlDecode(url);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 格式化minio文件名称
     *
     * @param file
     *            文件
     * @return
     */
    public static String extractFileNameMi(MultipartFile file) {
        // 获取文件后缀
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 按年/月/日创建目录
        String objectName = fileExtension + "/" + currentDate.getYear() + "/" + currentDate.getMonthValue() + "/"
            + currentDate.getDayOfMonth() + "/" + UUID.randomUUID().toString() + "." + fileExtension;
        return objectName;
    }

    /**
     * 设置不同浏览器编码
     *
     * @param filename
     *            文件名称
     * @param request
     *            请求对象
     */
    public static String filenameEncoding(String filename, HttpServletRequest request) throws Exception {
        // 获得请求头中的User-Agent
        String agent = request.getHeader("User-Agent");
        // 根据不同的客户端进行不同的编码
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            BASE64Encoder base64Encoder = new BASE64Encoder();
            filename = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 创建桶
     *
     * @param bucketName
     *            桶名称
     */
    public static void createBucket(String bucketName) {
        if (StringUtils.isBlank(bucketName)) {
            throw new RuntimeException("桶名不能为空！");
        }
        try {
            if (checkBucketExist(bucketName)) {
                throw new RuntimeException(bucketName + "桶已存在！");
            } else {
                utils.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("创建桶失败！", e);
        }
    }

    /**
     * 检查桶是否存在
     *
     * @param bucketName
     *            桶名称
     * @return boolean true-存在 false-不存在
     */
    public static boolean checkBucketExist(String bucketName) throws Exception {
        if (StringUtils.isBlank(bucketName)) {
            throw new RuntimeException("桶名不能为空！");
        }
        return utils.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 检测某个桶内是否存在某个文件
     *
     * @param bucketName
     *            桶名称
     * @param objectName
     *            文件名称
     */
    public static boolean getBucketFileExist(String bucketName, String objectName) {
        if (StringUtils.isBlank(objectName) || StringUtils.isBlank(bucketName)) {
            throw new RuntimeException("检测文件的时候，文件名和桶名不能为空！");
        }
        try {
            // 判断文件是否存在
            return (utils.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build()) && utils.minioClient
                .statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()) != null);
        } catch (Exception e) {
            throw new RuntimeException("该文件不存在!", e);
        }
    }

    public static void downloadFile(String fileUrl, HttpServletRequest request, HttpServletResponse response) {
        String bucketName = MinioConfig.getBucketName();
        downloadFile(bucketName, fileUrl, request, response);
    }

    /**
     * 下载文件
     *
     * @param bucketName
     *            桶名称
     * @param fileUrl
     *            文件源路径 url
     * @param response
     * @return
     */
    public static InputStream downloadFile(String bucketName, String fileUrl, HttpServletRequest request,
        HttpServletResponse response) {
        // 从第三个 / 截取url
        String substringFileUrl = substringOriginalName(fileUrl);
        // 检测桶和文件是否存在
        getBucketFileExist(bucketName, substringFileUrl);
        try {
            InputStream file =
                    utils.minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(substringFileUrl).build());
            // 处理文件名
            String filename = new String(substringFileUrl.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(substringFileUrl)) {
                filename = substringFileUrl;
            }
            // filenameEncoding 方法兼容不同浏览器编码
            response.addHeader("Content-Disposition", "attachment;fileName=" + filenameEncoding(fileUrl, request));
            // 设置响应头，告诉浏览器以附件形式下载文件并使用指定的文件名保存
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 从第三个斜杠截取文件路径
     *
     * @param originalName
     * @return
     */
    public static String substringOriginalName(String originalName) {
        // 使用split()方法按斜杠分割成字符串数组
        String[] parts = originalName.split("/");
        // 从第三个元素开始拼接成新的字符串
        StringBuilder modifiedFileName = new StringBuilder();
        for (int i = 4; i < parts.length; i++) {
            modifiedFileName.append(parts[i]);
            if (i < parts.length - 1) {
                modifiedFileName.append("/");
            }
        }
        return String.valueOf(modifiedFileName);
    }

    /**
     * 根据文件路径得到预览文件 绝对地址
     *
     * @param fileUrl
     *            文件名称
     * @return
     */
    public static String getPreviewFileUrl(String fileUrl) {
        String bucketName = MinioConfig.getBucketName();
        String previewFileUrl = getPreviewFileUrl(bucketName, fileUrl);

        return previewFileUrl;
    }

    /**
     * 根据文件路径得到预览文件 绝对地址
     *
     * @param bucketName
     *            桶名称
     * @param fileUrl
     *            文件名称
     * @return
     */
    public static String getPreviewFileUrl(String bucketName, String fileUrl) {
        String originalName = substringOriginalName(fileUrl);
        // 检测桶和文件是否存在
        getBucketFileExist(bucketName, originalName);
        // 设定预览到期时间（分钟）
        Integer previewExpiry = 10;
        try {
            utils.minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(originalName).build());
            return utils.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET)
                .bucket(bucketName).object(originalName).expiry(previewExpiry, TimeUnit.MINUTES).build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
