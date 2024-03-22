package com.ruoyi.common.core.domain;

import lombok.Data;

import java.util.List;

@Data
public class ApproveRequest {
    private Long id;

    private List<Long> ids;

    private String approvalResults;

    private String approvalComment;

}
