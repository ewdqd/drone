package com.houduan.drone.user.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UsersDTO;
import com.houduan.drone.Entity.Users;
import com.houduan.drone.user.Mapper.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User business service — credential validation and user CRUD.
 * JWT creation is handled by the gateway, not here.
 */
@Service
@Transactional
public class UsersServiceImpl {

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Validate credentials and return user info (without token).
     * The gateway will create the JWT after this returns.
     */
    public HttpResultResponse userLogin(String username, String password) {
        Users users = getUsersByUsername(username);
        if (users == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid username");
        }
        if (!passwordEncoder.matches(password, users.getPassword())) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid password");
        }
        UsersDTO userDTO = entityConvertToDTO(users);
        userDTO.setUserstatus(users.getUserstatus());
        return HttpResultResponse.success(userDTO);
    }

    public HttpResultResponse getUserById(Integer id) {
        Users users = usersDAO.selectById(id);
        if (users == null) {
            return HttpResultResponse.success(null);
        }
        UsersDTO dto = entityConvertToDTO(users);
        dto.setUserstatus(users.getUserstatus());
        return HttpResultResponse.success(dto);
    }

    public HttpResultResponse userRegister(UsersDTO usersDTO) {
        // 检查用户名是否已存在
        Users existing = getUsersByUsername(usersDTO.getUsername());
        if (existing != null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.CONFLICT.value())
                    .setMessage("username already exists");
        }
        Users users = new Users();
        users.setUsername(usersDTO.getUsername());
        users.setSex(usersDTO.getSex());
        users.setLoginid(usersDTO.getLoginid());
        users.setPhone(usersDTO.getPhone());
        users.setUserstatus(usersDTO.getUserstatus());
        users.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        usersDAO.insert(users);
        return HttpResultResponse.success();
    }

    public HttpResultResponse userModify(UsersDTO usersDTO) {
        Integer id = usersDTO.getId();
        if (id == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.BAD_REQUEST.value())
                    .setMessage("id is required");
        }
        Users users = usersDAO.selectById(id);
        if (users == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.NOT_FOUND.value())
                    .setMessage("user not found");
        }
        users.setLoginid(usersDTO.getLoginid());
        users.setPhone(usersDTO.getPhone());
        users.setUserstatus(usersDTO.getUserstatus());
        if (usersDTO.getPassword() != null && !usersDTO.getPassword().isEmpty()) {
            users.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        }
        users.setSex(usersDTO.getSex());
        users.setUsername(usersDTO.getUsername());
        usersDAO.updateById(users);
        return HttpResultResponse.success();
    }

    public HttpResultResponse userDelete(Integer id) {
        if (id == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.BAD_REQUEST.value())
                    .setMessage("id is required");
        }
        Users users = usersDAO.selectById(id);
        if (users == null) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.NOT_FOUND.value())
                    .setMessage("user not found");
        }
        usersDAO.deleteById(id);
        return HttpResultResponse.success();
    }

    public HttpResultResponse pageUsers(QueryDto queryDto) {
        return null; // will be handled by PageService merged here
    }

    private Users getUsersByUsername(String username) {
        return usersDAO.selectOne(new QueryWrapper<Users>().eq("username", username));
    }

    private UsersDTO entityConvertToDTO(Users users) {
        if (users == null) return null;
        return UsersDTO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .build();
    }
}
