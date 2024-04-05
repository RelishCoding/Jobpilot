package com.job.controller;

import com.job.dto.UserRegisterDTO;
import com.job.dto.VerifyCaptchaDTO;
import com.job.service.UserService;
import com.job.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/register/captcha")
    public Result sendCaptcha(@RequestParam String username, @RequestParam String email) {
        return userService.sendCaptcha(username, email);
    }

    @PostMapping("/register/verify")
    public Result verifyCaptcha(@RequestBody VerifyCaptchaDTO verifyCaptchaDTO) {
        return userService.verifyCaptcha(verifyCaptchaDTO);
    }

    @PostMapping("/register/submit")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.register(userRegisterDTO);
    }
}
