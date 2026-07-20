package com.houduan.drone.action.Service;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Context.LocalThreadHolder;
import com.houduan.drone.action.Mapper.ActionOperationMapper;
import com.houduan.drone.Common.ApiResult;
import com.houduan.drone.Entity.ActionOperation;
import com.houduan.drone.action.Service.ActionOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 行为操作业务逻辑接口实现类
 */
@Service
@Transactional
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
        Integer userId = LocalThreadHolder.getUserId();
        if (userId != null) {
            actionOperation.setUserId(userId);
        }
        save(actionOperation);
        return ApiResult.success();
    }
}
