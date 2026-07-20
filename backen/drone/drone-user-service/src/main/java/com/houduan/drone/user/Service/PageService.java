package com.houduan.drone.user.Service;

import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;



public interface PageService {
    HttpResultResponse rolePage(QueryDto queryDto);
}
