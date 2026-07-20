package com.houduan.drone.rank.Service;

import com.houduan.drone.Pojo.vo.DroneRankVO;
import com.houduan.drone.rank.Service.DroneRankService;
import com.houduan.drone.Utils.RedisOpsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DroneRankServiceImpl implements DroneRankService {

    private static final String RANK_KEY = "drone:flight:rank";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addFlightTime(String droneId, long seconds) {
        // 使用 ZINCRBY 原子递增飞行时长
        RedisOpsUtils.zIncrement(RANK_KEY, droneId, (double) seconds);
    }

    @Override
    public List<DroneRankVO> getTopRanking(int topN) {
        Set<ZSetOperations.TypedTuple<Object>> set =
                RedisOpsUtils.zRevRangeWithScores(RANK_KEY, 0, topN - 1);

        List<DroneRankVO> result = new ArrayList<>();
        if (set == null || set.isEmpty()) {
            return result;
        }

        int rank = 1;
        for (ZSetOperations.TypedTuple<Object> tuple : set) {
            result.add(DroneRankVO.builder()
                    .rank(rank++)
                    .droneId(String.valueOf(tuple.getValue()))
                    .totalSeconds(tuple.getScore())
                    .build());
        }

        return result;
    }
}
