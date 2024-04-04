package com.job.controller;

import com.job.service.UserService;
import com.job.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/register/captcha")
    public Result sendCaptcha(@RequestParam String username, @RequestParam String email) {
        return userService.sendCaptcha(username, email);
    }
}
