package com.houduan.drone.Service.rpc;

import com.houduan.drone.Common.Result;
import com.houduan.drone.Entity.ActionOperation;

/**
 * Dubbo RPC interface for action operation service.
 */
public interface ActionRpcService {

    Result<String> saveAction(ActionOperation actionOperation);
}
