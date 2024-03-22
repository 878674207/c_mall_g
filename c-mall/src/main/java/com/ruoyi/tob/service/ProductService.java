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

    ProductVo queryProductDetail(Long id) throws CommonException;

    void batchOperate(ProductQo productQo) throws CommonException;

    void productApprove(ApproveRequest approveRequest) throws CommonException;

    Page queryClientProductList(ClientProductQo clientProductQo);

    ProductVo queryClientProductDetail(Long id) throws CommonException;

    ConfirmOrderVo productSettle(SettleQo settleQo) throws CommonException;
}
