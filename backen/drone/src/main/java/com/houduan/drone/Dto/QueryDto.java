package com.houduan.drone.Dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询参数接收实体类基类，含有六项基础参数，实际使用时可以根据实际情况进行拓展
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryDto {
    private Integer id; // ID

    private List<Integer> ids; // ID列表

    private Integer current; // 当前页

    private Integer size; //页面数据大小

    private LocalDateTime startTime; //开始时间

    private LocalDateTime endTime; //结束时间

    private String username;//查询时的姓名

    private String sex;//查询时的性别
}

