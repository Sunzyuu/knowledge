package com.github.forest.service.impl;

import com.github.forest.service.JavaMailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author sunzy
 * @Date 2023/5/28 21:01
 */
@SpringBootTest
class JavaMailServiceImplTest {

    @Resource
    private JavaMailService javaMailService;

    @Test
    void sendEmailCode() {
        try {
            javaMailService.sendEmailCode("08183039@cumt.edu.cn");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendForgetPasswordEmail() {
        try {
            javaMailService.sendForgetPasswordEmail("08183039@cumt.edu.cn");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}