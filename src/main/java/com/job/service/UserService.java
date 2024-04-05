package com.job.service;

import com.job.dto.UserLoginDTO;
import com.job.dto.UserRegisterDTO;
import com.job.dto.VerifyCaptchaDTO;
import com.job.util.Result;

public interface UserService {
    Result sendCaptcha(String username, String email);

    Result verifyCaptcha(VerifyCaptchaDTO verifyCaptchaDTO);

    Result register(UserRegisterDTO userRegisterDTO);

    Result login(UserLoginDTO userLoginDTO);
}
