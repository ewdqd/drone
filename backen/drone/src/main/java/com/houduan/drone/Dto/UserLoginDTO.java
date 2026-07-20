package com.houduan.drone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @NonNull
    private String username;

    @NonNull
    private String password;

    /**
     * 前端历史遗留的 AES IV 字段，现在不再使用（密码通过 HTTPS 明文传输 + BCrypt 服务端加密）
     */
    private String iv;


}
