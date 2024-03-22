package com.ruoyi.common.service.impl;

import com.google.common.collect.Maps;
import com.ruoyi.common.entity.LocationInfo;
import com.ruoyi.common.mapper.LocationMapper;
import com.ruoyi.common.request.LocationRequest;
import com.ruoyi.common.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private static final String LEVEL1 = "1";

    private static final String LEVEL2 = "2";

    private static final String LEVEL3 = "3";

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public List queryLocationInfo(LocationRequest locationRequest) {
        if (StringUtils.isEmpty(locationRequest.getParentCode()) || LEVEL1.equals(locationRequest.getLevel())) {
            // 查询所有省
            return locationMapper.queryAllProvince();

        } else if (LEVEL2.equals(locationRequest.getLevel())) {
            // 根据省查询所有市
            return locationMapper.queryCityByProvinceCode(locationRequest.getParentCode());

        } else if (LEVEL3.equals(locationRequest.getLevel())) {
            // 根据市查询所有县/区
            return locationMapper.queryAreaByCityCode(locationRequest.getParentCode());
        }
        return Lists.newArrayList();
    }

    @Override
    public Map<String, String> queryLocationByName(String province, String city, String region) {
        HashMap<String, String> map = Maps.newHashMap();
        LocationInfo provinceInfo = locationMapper.queryProvinceByName(province);
        if (Objects.isNull(provinceInfo)) {
            return map;
        }
        map.put(provinceInfo.getName(),provinceInfo.getCode());
        LocationInfo cityInfo = locationMapper.queryCityByNameAndCodeP(city, provinceInfo.getCode());
        if (Objects.isNull(cityInfo)) {
            return map;
        }
        map.put(cityInfo.getName(),cityInfo.getCode());
        LocationInfo regionInfo = locationMapper.queryRegionByNameAndCodeC(region, cityInfo.getCode());
        if (Objects.isNull(regionInfo)) {
            return map;
        }
        map.put(regionInfo.getName(),regionInfo.getCode());
        return map;
    }
}
