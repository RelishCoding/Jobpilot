package com.job.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerifyCaptchaDTO implements Serializable {
    private String captcha;

    private String email;

    private String username;
}
