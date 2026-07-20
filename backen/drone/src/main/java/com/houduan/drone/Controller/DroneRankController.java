package com.houduan.drone.Controller;

import com.houduan.drone.Common.ApiResult;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Pojo.vo.DroneRankVO;
import com.houduan.drone.Service.DroneRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}")
public class DroneRankController {

    @Autowired
    private DroneRankService droneRankService;

    /**
     * 增加无人机飞行时长
     * @param droneId 无人机 ID
     * @param seconds 飞行秒数
     */
    @PostMapping("/drone/flight/add")
    public HttpResultResponse addFlightTime(@RequestParam String droneId,
                                             @RequestParam long seconds) {
        droneRankService.addFlightTime(droneId, seconds);
        return HttpResultResponse.success("飞行时长记录成功");
    }

    /**
     * 获取飞行时长排行榜 Top N
     * @param n 取前 N 名，默认 10
     */
    @GetMapping("/drone/ranking/top")
    public Result<List<DroneRankVO>> getTop(@RequestParam(defaultValue = "10") int n) {
        List<DroneRankVO> ranking = droneRankService.getTopRanking(n);
        return ApiResult.success(ranking);
    }
}
