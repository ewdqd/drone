package com.houduan.drone.Service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Entity.Users;
import com.houduan.drone.Mapper.UsersDAO;
import com.houduan.drone.Service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PageServiceImpl implements PageService {


    @Autowired
    private UsersDAO usersDAO;


    @Override
    public HttpResultResponse rolePage(QueryDto queryDto) {

        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryDto.getUsername())) {
            queryWrapper.eq(Users::getUsername, queryDto.getUsername());
        }
        if (StringUtils.isNotBlank(queryDto.getSex())) {
            queryWrapper.eq(Users::getSex, queryDto.getSex());
        }

        Page<Users> usersPage = usersDAO.selectPage(new Page<>(queryDto.getCurrent(), queryDto.getSize()), queryWrapper);
        List<Users> records = usersPage.getRecords();
        long total = usersPage.getTotal();
        map.put("records", records);
        map.put("total", total);

        return HttpResultResponse.success(map);


    }
}
