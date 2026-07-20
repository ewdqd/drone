package com.houduan.drone.Service.rpc;

import com.houduan.drone.Common.Result;
import com.houduan.drone.Pojo.vo.DroneRankVO;

import java.util.List;

/**
 * Dubbo RPC interface for drone ranking service.
 */
public interface RankRpcService {

    void addFlightTime(String droneId, long seconds);

    Result<List<DroneRankVO>> getTopRanking(int topN);
}
