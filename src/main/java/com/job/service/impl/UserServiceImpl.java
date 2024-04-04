package com.job.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.job.constant.MessageConstants;
import com.job.constant.RedisConstants;
import com.job.service.UserService;
import com.job.util.EmailUtil;
import com.job.util.RegexUtil;
import com.job.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EmailUtil emailUtil;

    @Override
    public Result sendCaptcha(String username, String email) {
        //校验邮箱是否合法
        if (RegexUtil.isEmailInvalid(email)) {
            // 如果不符合，返回错误信息
            return Result.fail(HttpStatus.FORBIDDEN.value(), MessageConstants.MAIL_FORMAT_ERROR);
        }

        // 符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 保存验证码到Redis，并设置有效期 5分钟
        stringRedisTemplate.opsForValue().set(RedisConstants.REGISTER_CODE_KEY + email, code, RedisConstants.REGISTER_CODE_TTL, TimeUnit.MINUTES);

        // 发送验证码
        String subject = "验证码";
        String text = "欢迎注册 Job pilot 平台，您的验证码是：" + code + "，5分钟之内有效";
        boolean success = emailUtil.sendEmail(email, subject, text);
        if (success) {
            log.info("发送验证码成功，验证码：{}", code);
            // 返回
            return Result.success();
        } else {
            return Result.fail(HttpStatus.SERVICE_UNAVAILABLE.value(), MessageConstants.MAIL_SEND_FAIL);
        }
    }
}
