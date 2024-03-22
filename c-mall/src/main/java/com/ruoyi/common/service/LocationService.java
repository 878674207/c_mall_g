package com.ruoyi.common.service;


import com.ruoyi.common.request.LocationRequest;

import java.util.List;
import java.util.Map;

public interface LocationService {
    List queryLocationInfo(LocationRequest locationRequest);

    Map<String,String> queryLocationByName(String province, String city, String region);
}
