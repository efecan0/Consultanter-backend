package com.example.demo.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    // Şifreyi hash ve salt'a çevirecek metod
    public static String[] hashPassword(String password) throws NoSuchAlgorithmException {
        // Güvenli bir salt üretme
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 16 byte'lık bir salt
        random.nextBytes(salt);

        // SHA-256 algoritması ile hash oluşturma
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt); // Salt'ı ekliyoruz
        byte[] hashedPassword = digest.digest(password.getBytes());

        // Salt ve hash'i Base64 formatında döndürüyoruz
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);

        return new String[] { saltBase64, hashBase64 };
    }

    // Hashlenmiş şifreyi ve salt'ı kullanarak doğrulama işlemi yapacak metod
    public static boolean verifyPassword(String password, String storedSalt, String storedHash) throws NoSuchAlgorithmException {
        // Salt'ı Base64 formatından byte[]'a çevirme
        byte[] salt = Base64.getDecoder().decode(storedSalt);

        // Şifreyi hash'leme
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt); // Salt'ı ekliyoruz
        byte[] hashedPassword = digest.digest(password.getBytes());

        // Hash'i Base64 formatına çevirme
        String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);

        // Eğer hesaplanan hash, saklanan hash ile eşleşiyorsa, şifre doğru demektir
        return hashBase64.equals(storedHash);
    }

}
