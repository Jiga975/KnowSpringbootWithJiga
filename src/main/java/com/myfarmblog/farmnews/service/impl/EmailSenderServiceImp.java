package com.myfarmblog.farmnews.service.impl;

import com.myfarmblog.farmnews.repository.UserRepository;
import com.myfarmblog.farmnews.service.EmailSenderService;
import com.myfarmblog.farmnews.utils.HelperClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailSenderServiceImp implements EmailSenderService {

    private final JavaMailSender mailSender;
    private final HelperClass helperClass;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String senderMail;

    @Override
    public void sendRegistrationEmailVerification(String url, String email, String name) {

    }
}
