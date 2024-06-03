package com.ruoyi.web.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.ResultStatusCode;
import com.ruoyi.common.core.response.FileResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.utils.file.FileUploadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: ucfx
 * @description: minio文件上传下载
 * @author: szq
 * @create: 2023-08-02 13:35
 **/
@Slf4j
@RestController
@RequestMapping("/file")
@Api(value = "文件管理", tags = {"文件管理"})
public class FileController {

    private static final String FILE_DELIMETER = ",";

    @Value("${minio.getUri}")
    private String getFileUri;

    @Value("${minio.url}")
    private String originalUri;

    /**
     * 上传到minio图片服务器(单文件)
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    @ApiOperation(value = "单文件上传", httpMethod = "POST")
    public Object uploadFileMinio(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            FileResponse response = new FileResponse();
            String originalFileName = file.getOriginalFilename();
            // 上传并返回访问地址
            String fileName = FileUploadUtils.uploadMinio(file);
            // 根据斜杠最后一次出现的位置截取子字符串
            String modifiedFileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            response.setFileUrl(fileName.replace(originalUri, getFileUri));
            response.setFileName(modifiedFileName);
            response.setOriginalFileName(originalFileName);
            response.setFileSize(file.getSize());
            return AjaxResult.success(response);
        } catch (Exception e) {
            log.error("upload failed,failed reason is {}", e);
            return new Result<>(ResultStatusCode.SYSTEM_ERR, null);
        }
    }


    /**
     * 上传到minio图片服务器(单文件)
     *
     * @param file
     * @return
     * @throws Exception
     */
//    @PostMapping("/upload")
//    @ApiOperation(value = "单文件上传", httpMethod = "POST")
//    public Object uploadFileMinio(@RequestParam("file") MultipartFile file) throws Exception {
//        try {
//            FileResponse response = new FileResponse();
//            String originalFileName = file.getOriginalFilename();
//            // 上传并返回访问地址
//            String fileName = FileUploadUtils.uploadMinio(file);
//            // 根据斜杠最后一次出现的位置截取子字符串
//            String modifiedFileName = fileName.substring(fileName.lastIndexOf('/') + 1);
//            response.setFileUrl(fileName);
//            response.setFileName(modifiedFileName);
//            response.setOriginalFileName(originalFileName);
//            response.setFileSize(file.getSize());
//            return AjaxResult.success(response);
//        } catch (Exception e) {
//            log.error("upload failed,failed reason is {}", e);
//            return new Result<>(ResultStatusCode.SYSTEM_ERR, null);
//        }
//    }

    /**
     * 上传到minio图片服务器(多文件)
     */
    @PostMapping("/uploads")
    @ApiOperation(value = "多文件上传", httpMethod = "POST")
    public Object uploadFiles(@RequestParam("file") List<MultipartFile> files) throws Exception {
        try {
            List<FileResponse> list = Lists.newArrayList();
            for (MultipartFile file : files) {
                FileResponse response = new FileResponse();
                String originalFileName = file.getOriginalFilename();
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.uploadMinio(file);
                // 根据斜杠最后一次出现的位置截取子字符串
                String modifiedFileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                response.setFileUrl(fileName);
                response.setFileName(modifiedFileName);
                response.setOriginalFileName(originalFileName);
                response.setFileSize(file.getSize());
                response.setFilePreviewUrl(FileUploadUtils.getPreviewFileUrl(fileName));
                list.add(response);
            }
            return AjaxResult.success(list);
        } catch (Exception e) {
            log.error("upload failed,failed reason is {}", e);
            return new Result<>(ResultStatusCode.SYSTEM_ERR, null);
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl  文件源路径
     * @param response
     * @return
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    public void downloadFile(@RequestParam("fileUrl") String fileUrl, HttpServletRequest request, HttpServletResponse response) {
        FileUploadUtils.downloadFile(fileUrl, request, response);
    }

    /**
     * 文件预览
     *
     * @param fileUrl 文件源路径
     * @return
     */
    @GetMapping("/preview")
    @ApiOperation(value = "文件预览", httpMethod = "GET")
    public String getPreviewFileUrl(String fileUrl) {
        return FileUploadUtils.getPreviewFileUrl(fileUrl);
    }
}
