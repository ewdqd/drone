package com.houduan.drone.user.Rpc;

import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UsersDTO;
import com.houduan.drone.Service.rpc.UserRpcService;
import com.houduan.drone.user.Service.UsersServiceImpl;
import com.houduan.drone.user.Service.PageServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class UserRpcServiceImpl implements UserRpcService {

    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private PageServiceImpl pageService;

    @Override
    public HttpResultResponse login(String username, String password) {
        return usersService.userLogin(username, password);
    }

    @Override
    public HttpResultResponse getUserById(Integer id) {
        return usersService.getUserById(id);
    }

    @Override
    public HttpResultResponse register(UsersDTO usersDTO) {
        return usersService.userRegister(usersDTO);
    }

    @Override
    public HttpResultResponse modify(UsersDTO usersDTO) {
        return usersService.userModify(usersDTO);
    }

    @Override
    public HttpResultResponse delete(Integer id) {
        return usersService.userDelete(id);
    }

    @Override
    public HttpResultResponse pageUsers(QueryDto queryDto) {
        return pageService.rolePage(queryDto);
    }
}
