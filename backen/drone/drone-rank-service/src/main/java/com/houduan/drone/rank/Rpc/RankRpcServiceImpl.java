package com.houduan.drone.rank.Rpc;

import com.houduan.drone.Common.Result;
import com.houduan.drone.Pojo.vo.DroneRankVO;
import com.houduan.drone.Service.rpc.RankRpcService;
import com.houduan.drone.rank.Service.DroneRankServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class RankRpcServiceImpl implements RankRpcService {

    @Autowired
    private DroneRankServiceImpl droneRankService;

    @Override
    public void addFlightTime(String droneId, long seconds) {
        droneRankService.addFlightTime(droneId, seconds);
    }

    @Override
    public Result<List<DroneRankVO>> getTopRanking(int topN) {
        return com.houduan.drone.Common.ApiResult.success(droneRankService.getTopRanking(topN));
    }
}
