package com.houduan.drone.action.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.houduan.drone.Dto.ActionOperationQueryDto;
import com.houduan.drone.Entity.ActionOperation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActionOperationMapper extends BaseMapper<ActionOperation> {
    List<ActionOperation> list(ActionOperationQueryDto dto);

}
