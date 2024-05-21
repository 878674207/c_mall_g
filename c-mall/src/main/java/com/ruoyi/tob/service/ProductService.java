package com.ruoyi.tob.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.ApproveRequest;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.qo.ProductQo;
import com.ruoyi.tob.vo.ProductVo;
import com.ruoyi.toc.qo.ClientProductQo;
import com.ruoyi.toc.qo.SettleQo;
import com.ruoyi.toc.vo.ConfirmOrderVo;

public interface ProductService {
    void saveProductInfo(ProductQo productQo);

    Page queryProductList(ProductQo productQo);

    String generateSn();

    ProductVo queryProductDetail(Long id);

    void batchOperate(ProductQo productQo);

    void productApprove(ApproveRequest approveRequest);

    Page queryClientProductList(ClientProductQo clientProductQo);

    ProductVo queryClientProductDetail(Long id);

    ConfirmOrderVo productSettle(SettleQo settleQo);
}
