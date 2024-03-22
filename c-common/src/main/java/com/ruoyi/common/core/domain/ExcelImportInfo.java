package com.ruoyi.common.core.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExcelImportInfo {
    private int successCount;

    private int failCount;

    private String msg;
}
