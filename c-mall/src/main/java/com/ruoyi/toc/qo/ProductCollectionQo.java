package com.ruoyi.toc.qo;

import com.ruoyi.common.core.qo.BaseQo;
import lombok.Data;

import java.util.List;

@Data
public class ProductCollectionQo extends BaseQo {

    private Long productCategoryId;

    private List<Long> ids;

}
