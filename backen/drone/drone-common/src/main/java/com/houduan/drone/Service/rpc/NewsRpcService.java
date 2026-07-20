package com.houduan.drone.Service.rpc;

import com.houduan.drone.Common.Result;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Entity.News;
import com.houduan.drone.Pojo.vo.NewsVO;

/**
 * Dubbo RPC interface for news service.
 */
public interface NewsRpcService {

    Result<String> saveNews(News news);

    Result<String> updateNews(News news);

    Result<String> deleteNews(Integer id);

    Result<NewsVO> getNewsById(Integer id);

    HttpResultResponse listNews(NewsQueryDto queryDto);

    Result<String> setShowStatus(News news);
}
