package com.myfarmblog.farmnews.utils;

import com.myfarmblog.farmnews.repository.UserRepository;
import com.myfarmblog.farmnews.securitty.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelperClass {
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;

    //NB the decryption key is saved in the local environmental variable, do so to avoid security breach while
    // releasing code to the wild
    @Value("${DECRYPTION_KEY}")
    private String key;

    @Value("${DECRYPTION_KEY}")
    private String iv;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
    public String decryptPassword(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encrypted1 = decoder.decode(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString.trim();
    }

}
