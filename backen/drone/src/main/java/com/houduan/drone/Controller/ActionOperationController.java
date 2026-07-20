package com.houduan.drone.Controller;


import com.houduan.drone.Common.Result;
import com.houduan.drone.Entity.ActionOperation;
import com.houduan.drone.Service.ActionOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/action-operation")
public class ActionOperationController {

    @Autowired
    private ActionOperationService actionOperationService;

    /**
     * 新增
     *
     * @param actionOperation 实体数据
     * @return Result<String>
     */
    @PostMapping(value = "/saveActionEntity")
    @ResponseBody
    public Result<String> saveEntity(@RequestBody ActionOperation actionOperation) {
        return actionOperationService.saveEntity(actionOperation);
    }
}




