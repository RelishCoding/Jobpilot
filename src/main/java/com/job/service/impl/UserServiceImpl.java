package com.job.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.job.constant.MessageConstants;
import com.job.constant.RedisConstants;
import com.job.dto.UserLoginDTO;
import com.job.dto.UserRegisterDTO;
import com.job.dto.VerifyCaptchaDTO;
import com.job.entity.User;
import com.job.mapper.UserMapper;
import com.job.service.UserService;
import com.job.util.EmailUtil;
import com.job.util.RegexUtil;
import com.job.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private UserMapper userMapper;

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
        stringRedisTemplate.opsForValue().set(RedisConstants.REGISTER_CODE_KEY + username, code, RedisConstants.REGISTER_CODE_TTL, TimeUnit.MINUTES);

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

    @Override
    public Result verifyCaptcha(VerifyCaptchaDTO verifyCaptchaDTO) {
        String username = verifyCaptchaDTO.getUsername();
        String captcha = verifyCaptchaDTO.getCaptcha();

        // 校验验证码格式
        if (RegexUtil.isCodeInvalid(captcha)) {
            return Result.fail(MessageConstants.CAPTCHA_FORMAT_ERROR);
        }

        String key = RedisConstants.REGISTER_CODE_KEY + username;
        String code = stringRedisTemplate.opsForValue().get(key);

        // 验证码已过期
        if (code == null) {
            return Result.fail(MessageConstants.CAPTCHA_TIMEOUT);
        }

        if (captcha.equals(code)) {
            return Result.success();
        } else {
            return Result.fail(MessageConstants.CAPTCHA_VERIFY_FAIL);
        }
    }

    @Override
    public Result register(UserRegisterDTO userRegisterDTO) {
        // 校验格式
        if (RegexUtil.isEmailInvalid(userRegisterDTO.getEmail())) {
            log.info("邮箱格式错误");
            return Result.fail(MessageConstants.MAIL_FORMAT_ERROR);
        }
        if (RegexUtil.isUsernameInvalid(userRegisterDTO.getUsername())) {
            log.info("用户名格式错误");
            return Result.fail(MessageConstants.USERNAME_FORMAT_ERROR);
        }
        if (RegexUtil.isPhoneInvalid(userRegisterDTO.getPhone())) {
            log.info("手机号格式错误");
            return Result.fail(MessageConstants.PHONE_FORMAT_ERROR);
        }
        if (RegexUtil.isPasswordInvalid(userRegisterDTO.getPassword())) {
            log.info("密码格式错误");
            return Result.fail(MessageConstants.PASSWORD_FORMAT_ERROR);
        }

        // 构造对象
        User user = new User();
        BeanUtil.copyProperties(userRegisterDTO, user);

        //todo 加密

        // 查询用户是否已存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", user.getUsername());
        boolean exists = userMapper.exists(userQueryWrapper);
        if (exists) {
            return Result.fail(MessageConstants.USER_EXISTS);
        }

        // 插入数据
        user.setState(1);
        userMapper.insert(user);
        return Result.success();
    }

    @Override
    public Result login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();
        log.info("userLoginDTO.password = {}", password);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        // 校验密码格式
        if (RegexUtil.isPasswordInvalid(password)) {
            return Result.fail(MessageConstants.PASSWORD_FORMAT_ERROR);
        }

        // 判断account是手机号、邮箱还是用户名
        if (!RegexUtil.isPhoneInvalid(account)) {
            userQueryWrapper.eq("phone", account);
        } else if (!RegexUtil.isEmailInvalid(account)) {
            userQueryWrapper.eq("email", account);
        } else if (!RegexUtil.isUsernameInvalid(account)) {
            userQueryWrapper.eq("username", account);
        } else {
            return Result.fail(MessageConstants.ACCOUNT_FORMAT_ERROR);
        }

        // 查询用户是否存在
        List<User> userList = userMapper.selectList(userQueryWrapper);
        if (CollectionUtil.isEmpty(userList)) {
            return Result.fail(MessageConstants.USER_NOT_EXIST);
        }

        // 校验密码
        for (User user : userList) {
            log.info("user.password = {}", user.getPassword());
            if (user.getPassword().equals(password)) {
                Integer userId = user.getUserId();
                // 更改用户登录状态
                user.setState(1);
                userMapper.updateById(user);
                HashMap<String, Integer> map = new HashMap<>();
                map.put("user_id", userId);
                return Result.success(map);
            }
        }

        // 密码错误
        return Result.fail(MessageConstants.PASSWORD_ERROR);
    }
}
