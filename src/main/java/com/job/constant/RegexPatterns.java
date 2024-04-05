package com.job.constant;

public class RegexPatterns {
    // 手机号正则，前三位号码段有要求
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    // 邮箱正则
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // 验证码正则，6位数字或字母
    public static final String CAPTCHA_REGEX = "^[a-zA-Z\\d]{6}$";

    // 密码正则，8~16位非连续或重复的字符，至少有一个字母、一个数字、一个特殊字符
    //public static final String PASSWORD_REGEX = "^(?!.*(.)\\1)[^a-zA-Z0-9]{8,16}$";
    public static final String PASSWORD_REGEX = "^(?!.*(.)\\1)(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*?])[a-zA-Z\\d!@#$%^&*?]{8,16}$";

    // 用户名正则，4-20个字符，支持汉字、字母、数字2种及以上组合
    public static final String USERNAME_REGEX = "^(?=.*[\\u4E00-\\u9FFFa-zA-Z])[0-9\\u4E00-\\u9FFFa-zA-Z]{4,20}$";
}
