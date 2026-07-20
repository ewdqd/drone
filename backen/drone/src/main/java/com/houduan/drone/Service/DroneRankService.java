package com.houduan.drone.Service;

import com.houduan.drone.Pojo.vo.DroneRankVO;

import java.util.List;

/**
 * 无人机飞行时长排行榜服务接口
 */
public interface DroneRankService {

    /**
     * 增加无人机的飞行时长（秒）
     * @param droneId 无人机 ID
     * @param seconds 飞行秒数
     */
    void addFlightTime(String droneId, long seconds);

    /**
     * 获取飞行时长排行榜 Top N
     * @param topN 取前 N 名
     * @return 排名列表
     */
    List<DroneRankVO> getTopRanking(int topN);
}
