package com.houduan.drone.Controller;


import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UsersDTO;
import com.houduan.drone.Mapper.UsersDAO;
import com.houduan.drone.Service.PageService;
import com.houduan.drone.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserListController {


    @Autowired
    private PageService pageService;

   @Autowired
   private UsersService usersService;

    @PostMapping("/updatauser")
    public HttpResultResponse getUserList(@RequestBody QueryDto queryDto) {

        return pageService.rolePage(queryDto);
    }



    @PostMapping("/adduser")
    public HttpResultResponse userAdd(@RequestBody UsersDTO usersDTO) {

        return  usersService.userRegister(usersDTO);
    }


    @PostMapping("/userModify")
    public HttpResultResponse userModify(@RequestBody UsersDTO usersDTO) {

        return  usersService.userModify(usersDTO);
    }

    @PostMapping("/userDelete")
    public HttpResultResponse userDelete(@RequestBody Integer id) {
        return  usersService.userDelete(id);
    }

}
