package com.lzbz.bff.util;

import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {
    private static SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES";

    @Value("${encryption.secret-key}")
    private String secretKeyString;

    @PostConstruct
    public void init() {
        prepareSecretKey(secretKeyString);
    }

    private void prepareSecretKey(String myKey) {
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(Long codigoUnico) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(codigoUnico.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static Long decrypt(String encryptedCodigoUnico) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return Long.parseLong(new String(cipher.doFinal(Base64.getDecoder().decode(encryptedCodigoUnico))));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
