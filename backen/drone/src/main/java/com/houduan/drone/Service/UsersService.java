package com.houduan.drone.Service;



import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UsersDTO;
import java.util.Optional;


public interface UsersService {
    HttpResultResponse userLogin(String username, String password);

    Optional<UsersDTO> refreshToken(String token);

    HttpResultResponse userRegister(UsersDTO usersDTO);

    HttpResultResponse userModify(UsersDTO usersDTO);

    HttpResultResponse userDelete(Integer id);
}
