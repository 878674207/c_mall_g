package com.ruoyi.common.core.qo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import lombok.Data;

import java.util.Objects;


@Data
public class BaseQo<T> {
    private Long pageNum;
    private Long pageSize;

    private String keyword;

    /**
     *  mybatis-plus 分页对象
     * @return
     */
    public Page<T> initPage() {
        if (Objects.isNull(this.pageNum) || Objects.isNull(this.pageSize)) {
            return new Page<>(1,10);
        }
        return new Page<>(this.pageNum,this.pageSize);
    }

    /**
     *  mybatis插件分页PageHelper
     */
    public void startPage() {
        PageHelper.startPage(pageNum.intValue(), pageSize.intValue());
    }
}
