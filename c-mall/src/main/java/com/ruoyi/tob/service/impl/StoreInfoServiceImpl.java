package com.ruoyi.tob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.RoleConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.tob.entity.StoreFile;
import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.tob.mapper.StoreMapper;
import com.ruoyi.tob.service.StoreFileService;
import com.ruoyi.tob.service.StoreInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lis
 * @Description:
 * @date 2023/12/27
 */
@RequiredArgsConstructor
@Service
public class StoreInfoServiceImpl extends ServiceImpl<StoreMapper, StoreInfo> implements StoreInfoService {
    private final StoreMapper storeMapper;

    private final ISysUserService sysUserService;

    private final StoreFileService storeFileService;
    private final SysUserRoleMapper userRoleMapper;

    //创建用用户默认密码
    private static final String DEFAULTPASSWORD = "123456";

    /**
     * 分页展示店铺信息
     *
     * @param storeInfo
     * @param page
     * @return
     */
    @Override
    public Page<StoreInfo> show(StoreInfo storeInfo, BaseQo page) {
        LambdaQueryWrapper<StoreInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(storeInfo.getStoreAccount()), StoreInfo::getStoreAccount, storeInfo.getStoreAccount())
                .eq(StringUtils.isNotEmpty(storeInfo.getStoreType()), StoreInfo::getStoreType, storeInfo.getStoreType())
                .eq(StringUtils.isNotEmpty(storeInfo.getStoreStatus()), StoreInfo::getStoreStatus, storeInfo.getStoreStatus())
                .like(StringUtils.isNotEmpty(storeInfo.getStoreContactPerson()), StoreInfo::getStoreContactPerson, storeInfo.getStoreContactPerson())
                .ge(StringUtils.isNotEmpty(storeInfo.getStartTime()), StoreInfo::getAddTime, storeInfo.getStartTime())
                .le(StringUtils.isNotEmpty(storeInfo.getEndTime()), StoreInfo::getAddTime, storeInfo.getEndTime());
        Page page1 = page.initPage();
        Page<StoreInfo> result = storeMapper.selectPage(page1, wrapper);
        Map<Long, List<StoreFile>> fileMap = storeFileService.list().stream().collect(Collectors.groupingBy(StoreFile::getStoreId));
        for (Long storeId : fileMap.keySet()) {
            for (StoreInfo record : result.getRecords()) {
                if (record.getId().equals(storeId)) {
                    record.setAttest(fileMap.get(storeId));
                }
            }
        }
        return result;
    }

    /**
     * 店铺新增
     *
     * @param storeInfo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreInfo saveStore(StoreInfo storeInfo) {
        //为店铺生成平台账号
        if (StringUtils.isNotEmpty(storeInfo.getStoreAccount())) {
            if (checkDuplicate(storeInfo.getStoreAccount()) > 0) throw new ServiceException("该账号已被注册,请重新填写");
        } else {
            String numb = "store_" + ranNumb(6);
            for (int i = 0; checkDuplicate(numb)>0; i++) {
                numb="store_" + ranNumb(6);
            }
            storeInfo.setStoreAccount(numb);
        }
        //判断商铺名称是否存在
        LambdaQueryWrapper<StoreInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreInfo::getStoreName, storeInfo.getStoreName());
        if (storeMapper.selectCount(wrapper) > 0) throw new ServiceException("商铺名称已存在,请更改");
        //保存店铺信息
        storeInfo.setAddTime(new Date());
        storeInfo.setStoreName(storeInfo.getStoreAccount());
        save(storeInfo);
        //保存相关资产证明
        for (StoreFile storeFile : storeInfo.getAttest()) {
            storeFile.setStoreId(storeInfo.getId());
        }
        storeFileService.saveBatch(storeInfo.getAttest());
        //为该店铺创建登录账户
        SysUser user = new SysUser();
        user.setNickName(storeInfo.getStoreAccount());
        user.setUserName(storeInfo.getStoreAccount());
        user.setPassword(SecurityUtils.encryptPassword(DEFAULTPASSWORD));
        user.setPhonenumber(storeInfo.getContactPhone());
        user.setEmail(storeInfo.getContactMail());
        sysUserService.insertUser(user);
        storeInfo.setPassword(DEFAULTPASSWORD);
        //为用户赋角色
        Long roleId = userRoleMapper.selectByRoleKey(RoleConstants.MALL_STORE_MANAGER).getRoleId();
        SysUserRole one = new SysUserRole();
        one.setRoleId(roleId);
        one.setUserId(user.getUserId());
        List<SysUserRole> list = new ArrayList<>();
        list.add(one);
        userRoleMapper.batchUserRole(list);
        return storeInfo;}

    /**
     * 修改店铺信息
     * @param storeInfo
     */
    @Override
    public void updateStore(StoreInfo storeInfo) {
        LambdaUpdateWrapper<StoreInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(StoreInfo::getId,storeInfo.getId())
                .set(StoreInfo::getStoreName,storeInfo.getStoreName())
                .set(StoreInfo::getLogo,storeInfo.getLogo())
                .set(StoreInfo::getMainBusiness,storeInfo.getMainBusiness());
        update(updateWrapper);
    }

    /**
     * 获取当前店铺店铺信息
     * @return
     */
    @Override
    public StoreInfo currentStoreInfo() {
        LambdaUpdateWrapper<StoreInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StoreInfo::getStoreAccount,SecurityUtils.getLoginUser().getUsername());
        return  getOne(wrapper);
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    @Override
    public StoreInfo selectById(String id) {
        StoreInfo storeInfo = storeMapper.selectById(id);
        LambdaUpdateWrapper<StoreFile> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StoreFile::getStoreId,id);
        List<StoreFile> files = storeFileService.list(wrapper);
        if (files.size()>0)storeInfo.setAttest(files);
        return storeInfo;
    }
    /**
     * 店铺状态更改
     * @param id
     * @return
     */
    @Override
    public void forbid(String id, String storeStatus,String forbidReason) {
        StoreInfo one = getById(id);
        if ("0".equals(storeStatus)){
            one.setStoreStatus(storeStatus);
            one.setForbidReason(null);
        }
        else if ("1".equals(storeStatus)){
            one.setForbidReason(forbidReason);
            one.setStoreStatus(storeStatus);
        }
        updateById(one);
    }

    /**
     * 检查账号是否重复
     *
     * @param storeAccount
     * @return
     */
    private Long checkDuplicate(String storeAccount) {
        LambdaQueryWrapper<StoreInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreInfo::getStoreAccount, storeAccount);
        return storeMapper.selectCount(wrapper);
    }

    /**
     * 随机生成账号
     *
     * @param length
     * @return
     */
    private String ranNumb(Integer length) {
        String uid = "";
        for (Integer i = 0; i < length; i++) {
            String randChar = String.valueOf(Math.round(Math.random() * 9));
            uid = uid.concat(randChar);
        }
        return uid;
    }


}
