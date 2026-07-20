package com.houduan.drone.Pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 无人机排行榜 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroneRankVO {

    /**
     * 排名（从 1 开始）
     */
    private Integer rank;

    /**
     * 无人机 ID
     */
    private String droneId;

    /**
     * 总飞行时长（秒）
     */
    private Double totalSeconds;
}
