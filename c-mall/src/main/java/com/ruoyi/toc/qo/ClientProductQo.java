package com.ruoyi.toc.qo;

import com.ruoyi.common.core.qo.BaseQo;
import lombok.Data;

@Data
public class ClientProductQo extends BaseQo {

    // 2->按销量；3->价格从低到高；4->价格从高到低
    private Integer sort;

    private Long productCategoryId;

    private Long exStoreId;


    private Long storeId;
}
