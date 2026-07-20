package com.houduan.drone.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value="news")  // 指定对应的数据库表名为"news"
public class News {

    @TableId(type = IdType.AUTO)  // 主键类型设置为自增
    private Integer id;

    private Integer newsTypeId;

    private String title;

    private String content;

    private String cover;

    private String summary;

    private Boolean isShow;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

//    private Date updateTime;
}
