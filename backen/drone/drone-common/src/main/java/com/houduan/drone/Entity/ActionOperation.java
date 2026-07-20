package com.houduan.drone.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "action")
public class ActionOperation implements java.io.Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
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
     * 停留时间（只有当行为操作是停留时才需要设置）
     */
    private Long times;
    /**
     * 发布时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
