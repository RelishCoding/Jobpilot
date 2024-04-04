package com.job;

import com.job.util.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTest {
    @Autowired
    private EmailUtil emailUtil;

    @Test
    public void testSendEmail() {
        String to = "2806894439@qq.com";
        String subject = "验证码";
        String text = "测试发送验证码到邮箱";
        emailUtil.sendEmail(to, subject, text);
    }
}
