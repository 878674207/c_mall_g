package com.ruoyi.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {

    private Long total;
    private int pageNum;
    private int pageSize;
    private Long totalPage;
}
