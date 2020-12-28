package com.ymx.driver.map;

import com.amap.api.location.AMapLocation;

public interface ServerLocation {
    void success(AMapLocation amapLocation);

    void fails();
}
