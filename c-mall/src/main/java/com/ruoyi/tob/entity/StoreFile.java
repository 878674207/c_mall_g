package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName("store_file")
public class StoreFile {
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    @ApiModelProperty(value = "店铺id")
    @TableField(value = "store_id")
    private Long storeId;
    @ApiModelProperty(value = "文件路径")
    @TableField(value = "file_path")
    private String filePath;
}
