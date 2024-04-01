package com.myfarmblog.farmnews.service;

public interface EmailSenderService {
    void sendRegistrationEmailVerification(String url, String email, String name);
}
