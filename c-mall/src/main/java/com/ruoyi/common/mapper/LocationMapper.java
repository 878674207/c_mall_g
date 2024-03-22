package com.ruoyi.common.mapper;

import com.ruoyi.common.entity.LocationInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocationMapper {

    List<LocationInfo> queryAllProvince();

    List<LocationInfo> queryCityByProvinceCode(String code);


    List<LocationInfo> queryAreaByCityCode(String code);

    LocationInfo queryProvinceByName(String province);

    LocationInfo queryCityByNameAndCodeP(@Param("city") String city, @Param("provinceCode") String provinceCode);

    LocationInfo queryRegionByNameAndCodeC(@Param("region") String region, @Param("cityCode") String cityCode);
}
