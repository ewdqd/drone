package com.houduan.drone.Controller;

import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.UserLoginDTO;
import com.houduan.drone.Dto.UsersDTO;
import com.houduan.drone.Pojo.em.CommonErrorEnum;
import com.houduan.drone.Service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.houduan.drone.Config.AuthInterceptor.PARAM_TOKEN;


@RestController
@RequestMapping("${url.manage.prefix}${url.manage.version}")
public class LoginController {

    @Autowired
    private UsersService usersService;



    @PostMapping("/login")
    public HttpResultResponse login(@RequestBody UserLoginDTO loginDTO){
        // 直接取明文密码（前端通过 HTTPS 传输，不再做 AES 加密）
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        return usersService.userLogin(username, password);

    }

    @PostMapping("/token/refresh")
    public HttpResultResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("刷新了一次token");
        String token = request.getHeader(PARAM_TOKEN);
        Optional<UsersDTO> user = usersService.refreshToken(token);

        if (user.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return HttpResultResponse.error(CommonErrorEnum.NO_TOKEN.getMessage());
        }
        return HttpResultResponse.success(user.get());
    }

}
