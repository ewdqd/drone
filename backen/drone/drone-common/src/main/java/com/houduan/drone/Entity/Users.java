package com.houduan.drone.Entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("users")
public class Users implements java.io.Serializable {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("loginid")
    private String loginid;

    @TableField("sex")
    private String sex;

    @TableField("password")
    private String password;

    @TableField("phone")
    private String phone;

    @TableField("userstatus")
    private String userstatus;




}
