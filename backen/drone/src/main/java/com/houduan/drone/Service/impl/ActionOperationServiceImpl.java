package com.houduan.drone.Service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Context.LocalThreadHolder;
import com.houduan.drone.Mapper.ActionOperationMapper;
import com.houduan.drone.Common.ApiResult;
import com.houduan.drone.Entity.ActionOperation;
import com.houduan.drone.Service.ActionOperationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 行为操作业务逻辑接口实现类
 */
@Service
public class ActionOperationServiceImpl extends ServiceImpl<ActionOperationMapper, ActionOperation> implements ActionOperationService {


    /**
     * 新增
     *
     * @param actionOperation 实体
     * @return Result<String>
     */
    @Override
    public Result<String> saveEntity(ActionOperation actionOperation) {
        actionOperation.setCreateTime(LocalDateTime.now()); // 新增时间
//        actionOperation.setUserId(LocalThreadHolder.getUserId());
        save(actionOperation);
        return ApiResult.success();
    }
}
