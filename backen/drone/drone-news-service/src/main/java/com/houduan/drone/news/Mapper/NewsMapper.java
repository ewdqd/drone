package com.houduan.drone.news.Mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Entity.News;


import com.houduan.drone.Pojo.vo.NewsListItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//新闻的mapper
public interface NewsMapper extends BaseMapper<News> {


    Integer listPageCount(NewsQueryDto newsQueryDto);

}
