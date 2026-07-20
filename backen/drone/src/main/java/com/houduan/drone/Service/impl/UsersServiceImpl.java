package com.houduan.drone.Service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.houduan.drone.Common.CustomClaim;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UsersDTO;
import com.houduan.drone.Entity.Users;
import com.houduan.drone.Mapper.UsersDAO;
import com.houduan.drone.Service.UsersService;
import com.houduan.drone.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

   @Autowired
   private UsersDAO usersDAO;

   @Autowired
   private BCryptPasswordEncoder passwordEncoder;

    @Override
    public HttpResultResponse userLogin(String username, String password){

        Users users=this.getUsersByUsersname(username);
        if(users==null){
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid username");
        }
        // 使用 BCrypt 进行密码校验，替代明文比对
        if (!passwordEncoder.matches(password, users.getPassword())) {
            return new HttpResultResponse()
                    .setCode(HttpStatus.UNAUTHORIZED.value())
                    .setMessage("invalid password");
        }

        // JWT 中只存 userId、role 和 username，不再存密码
        Integer role = Integer.valueOf(users.getUserstatus());
        String token = JwtUtils.createToken(users.getId(), role, users.getUsername());
        UsersDTO userDTO = entityConvertToDTO(users);
        userDTO.setAccessToken(token);
        return HttpResultResponse.success(userDTO);

    }

    @Override
    public Optional<UsersDTO> refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Optional.empty();
        }
        CustomClaim customClaim;
        try {
            DecodedJWT jwt = JwtUtils.verifyToken(token);
            customClaim = new CustomClaim(jwt.getClaims());
        } catch (TokenExpiredException e) {
            customClaim = new CustomClaim(JWT.decode(token).getClaims());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        // 使用 userId 查询用户，不再依赖 username
        String refreshToken = JwtUtils.createToken(customClaim.getUserId(), customClaim.getRole(), customClaim.getUsername());

        UsersDTO user = entityConvertToDTO(usersDAO.selectById(customClaim.getUserId()));
        if (Objects.isNull(user)) {
            return Optional.empty();
        }
        user.setAccessToken(refreshToken);
        return Optional.of(user);
    }

    @Override
    public HttpResultResponse userRegister(UsersDTO usersDTO) {
        Users users=new Users();
        users.setUsername(usersDTO.getUsername());
        users.setSex(usersDTO.getSex());
        users.setLoginid(usersDTO.getLoginid());
        users.setPhone(usersDTO.getPhone());
        users.setUserstatus(usersDTO.getUserstatus());
        // 注册时对密码进行 BCrypt 加密存储
        users.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        usersDAO.insert(users);

        return HttpResultResponse.success();
    }

    @Override
    public HttpResultResponse userModify(UsersDTO usersDTO) {
        Integer id = usersDTO.getId();
        Users users = usersDAO.selectById(id);
        if(users!=null){
            users.setLoginid(usersDTO.getLoginid());
            users.setPhone(usersDTO.getPhone());
            users.setUserstatus(usersDTO.getUserstatus());
            // 修改密码时同样进行 BCrypt 加密
            if (usersDTO.getPassword() != null && !usersDTO.getPassword().isEmpty()) {
                users.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
            }
            users.setSex(usersDTO.getSex());
            users.setUsername(usersDTO.getUsername());
            usersDAO.updateById(users);
        }


        return HttpResultResponse.success();
    }

    @Override
    public HttpResultResponse userDelete(Integer id) {
         usersDAO.deleteById(id);
        return HttpResultResponse.success();
    }


    private Users getUsersByUsersname(String username){

        return usersDAO.selectOne(new QueryWrapper<Users>().eq("username", username));

    }

    private UsersDTO entityConvertToDTO(Users users) {
        if (users == null) {
            return null;
        }
        return UsersDTO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .build();
    }

}
