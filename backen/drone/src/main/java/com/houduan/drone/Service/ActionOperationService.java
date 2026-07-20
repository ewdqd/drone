package com.houduan.drone.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Entity.ActionOperation;

public interface ActionOperationService extends IService<ActionOperation> {
    Result<String> saveEntity(ActionOperation actionOperation);


}
