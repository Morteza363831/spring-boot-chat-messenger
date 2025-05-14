package com.example.messenger.utility;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMUtil {

    private static final int AES_KEY_SIZE = 256; // AES-256
    private static final int GCM_IV_LENGTH = 12; // Recommended IV size for GCM
    private static final int GCM_TAG_LENGTH = 128; // Authentication Tag size

    // Generate a new AES key
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }

    // Encrypt message
    public static String encrypt(String plaintext, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] encryptedData = cipher.doFinal(plaintext.getBytes());
        byte[] combined = new byte[iv.length + encryptedData.length];

        // Combine IV + Encrypted Data
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypt message
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedText);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedData = new byte[decodedData.length - GCM_IV_LENGTH];

        // Split IV and Encrypted Data
        System.arraycopy(decodedData, 0, iv, 0, iv.length);
        System.arraycopy(decodedData, iv.length, encryptedData, 0, encryptedData.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData);
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

}
