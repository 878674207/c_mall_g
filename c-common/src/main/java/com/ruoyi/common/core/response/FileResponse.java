package com.ruoyi.common.core.response;

import lombok.Data;

/**
 * @ClassName FileResponse
 * @Description TOD0
 * author axx
 * Date 2023/9/6 14:21
 * Version 1.0
 **/
@Data
public class FileResponse {
    //图片的url
    private String fileUrl;
    //图片的uuid的名称
    private String fileName;
    //图片的真实名字
    private String originalFileName;
    //图片的大小
    private Long fileSize;

    // 预览url
    private String filePreviewUrl;
}
