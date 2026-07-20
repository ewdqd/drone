package com.houduan.drone.Dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/* 行为操作查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActionOperationQueryDto extends QueryDto {
    /**
     * 用户ID，外键，关联的是用户表，标识哪个用户产生的行为记录
     */
    private Integer userId;
    /**
     * 新闻ID，外键，关联的是新闻表，标识哪篇新闻产生的行为操作
     */
    private Integer newsId;
    /**
     * 行为类型（1：点赞行为；2：阅读行为；3：收藏行为；4：停留行为）
     */
    private Integer type;
    /**
     * 查询的天数
     */
    private Integer days;
}

