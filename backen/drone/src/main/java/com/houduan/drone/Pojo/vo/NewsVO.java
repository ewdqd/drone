package com.houduan.drone.Pojo.vo;


import com.houduan.drone.Entity.News;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 新闻VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsVO extends News {
    /**
     * 新闻类别名
     */
    private String newsTypeName;
}
