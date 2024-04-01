package com.myfarmblog.farmnews.events.listener;

import com.myfarmblog.farmnews.events.event.CompleteRegistrationEvent;
import com.myfarmblog.farmnews.securitty.JwtTokenProvider;
import com.myfarmblog.farmnews.service.EmailSenderService;
import com.myfarmblog.farmnews.utils.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class CompleteRegistrationEventListener implements ApplicationListener<CompleteRegistrationEvent> {
    private final EmailSenderService emailSenderService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onApplicationEvent(CompleteRegistrationEvent event) {
        //create a verification token for the user
        String verificationToken = tokenProvider.generateSignUpVerificationToken(event.getEmail(), SecurityConstants.JWT_EXPIRATION);

        //build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "auth/verify-email?email=" + event.getEmail() + "&token=" + verificationToken;

        //send email to the user
        emailSenderService.sendRegistrationEmailVerification(url, event.getEmail(), event.getName());

        log.info("click the link to verify your email and change your password : {}",url);
    }
}
