package com.houduan.drone.Dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {


    private Integer id;

    private String username;

    private String sex;

    private String loginid;

    private String phone;

    private String userstatus;

    private String password;

    @JsonProperty("access_token")
    private String accessToken;



}
