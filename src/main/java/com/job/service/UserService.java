package com.job.service;

import com.job.util.Result;

public interface UserService {
    Result sendCaptcha(String username, String email);
}
