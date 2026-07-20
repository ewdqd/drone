package com.houduan.drone.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "notice")
public class Notice {

    @TableId(type= IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
}
