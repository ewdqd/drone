package com.houduan.drone.action.Rpc;

import com.houduan.drone.Common.Result;
import com.houduan.drone.Entity.ActionOperation;
import com.houduan.drone.Service.rpc.ActionRpcService;
import com.houduan.drone.action.Service.ActionOperationServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ActionRpcServiceImpl implements ActionRpcService {

    @Autowired
    private ActionOperationServiceImpl actionOperationService;

    @Override
    public Result<String> saveAction(ActionOperation actionOperation) {
        return actionOperationService.saveEntity(actionOperation);
    }
}
