package com.ruoyi.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {
    private List<T> data;
    private PageInfo pageInfo;

    public static PageResult getPageResult(List data, PageInfo pageInfo) {
        return new PageResult(data, pageInfo);
    }

    public static PageResult getPageResult() {
        return new PageResult();
    }

}
