package com.houduan.drone.news.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Entity.News;


import com.houduan.drone.Pojo.vo.NewsVO;

import java.util.List;

public interface NewsService  {
    Result<String> saveEntity(News news);

    Result<String> updateEntity(News news);

    Result<String> deleteByNewsId(Integer id);

    Result<NewsVO> getByNewsId(Integer id);

   HttpResultResponse list(NewsQueryDto newsQueryDto);

    Result<String> setShowStatus(News news);
}
