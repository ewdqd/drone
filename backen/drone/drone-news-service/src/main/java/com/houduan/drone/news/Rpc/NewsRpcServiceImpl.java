package com.houduan.drone.news.Rpc;

import com.houduan.drone.Common.Result;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Entity.News;
import com.houduan.drone.Pojo.vo.NewsVO;
import com.houduan.drone.Service.rpc.NewsRpcService;
import com.houduan.drone.news.Service.NewsServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class NewsRpcServiceImpl implements NewsRpcService {

    @Autowired
    private NewsServiceImpl newsService;

    @Override
    public Result<String> saveNews(News news) {
        return newsService.saveEntity(news);
    }

    @Override
    public Result<String> updateNews(News news) {
        return newsService.updateEntity(news);
    }

    @Override
    public Result<String> deleteNews(Integer id) {
        return newsService.deleteByNewsId(id);
    }

    @Override
    public Result<NewsVO> getNewsById(Integer id) {
        return newsService.getByNewsId(id);
    }

    @Override
    public HttpResultResponse listNews(NewsQueryDto queryDto) {
        return newsService.list(queryDto);
    }

    @Override
    public Result<String> setShowStatus(News news) {
        return newsService.setShowStatus(news);
    }
}
