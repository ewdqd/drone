package com.houduan.drone.Controller;



import com.houduan.drone.Common.Result;

import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Entity.News;
import com.houduan.drone.Pojo.vo.NewsVO;

import com.houduan.drone.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping(value = "/news/saveNewsEntity")
    public Result<String> saveEntity(@RequestBody News news) {
        return newsService.saveEntity(news);
    }

    @PutMapping(value = "/news/updateNewsEntity")
    public Result<String> updateEntity(@RequestBody News news) {
        return newsService.updateEntity(news);
    }

    @DeleteMapping(value = "/news/{id}")
    public Result<String> DeleteByNewsId(@PathVariable(value = "id") Integer id) {
        return newsService.deleteByNewsId(id);
    }

    @GetMapping(value = "/news/{id}")
    public Result<NewsVO> getByNewsId(@PathVariable(value = "id") Integer id) {
        return newsService.getByNewsId(id);
    }

//    @Pager
    @PostMapping(value="/news/list")
    public HttpResultResponse list(@RequestBody NewsQueryDto newsQueryDto) {
//        System.out.println("1111111111" + newsQueryDto.getCurrent());
        return newsService.list(newsQueryDto);
    }

   @PostMapping(value = "/news/setShowStatus")
   public Result<String> setShowStatus(@RequestBody News news) {
        return newsService.setShowStatus(news);
   }

}
