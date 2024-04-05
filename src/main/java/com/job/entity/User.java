package com.job.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    @TableId("user_id") //指定主键
    private Integer userId;

    private String username;

    private String password;

    private String email;

    private String phone;

    private Integer state;
}
