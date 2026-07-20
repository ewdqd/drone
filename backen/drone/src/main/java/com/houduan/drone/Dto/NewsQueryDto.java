package com.houduan.drone.Dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 新闻查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsQueryDto extends QueryDto {
    /**
     * 新闻类别ID
     */
    private Integer newsTypeId;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否展示
     */
    private Boolean isShow;
}
