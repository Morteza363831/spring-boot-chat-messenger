package com.example.messengercommand.utility;

import com.example.messengercommand.exceptions.UnknownException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {

    private final static String ALGORITHM = "AES";
    private final static byte[] SECRET = {-66, 38, -104, 107, -44, 110, 120, -123, -96, -96, -57, -117, 84, 109, 127, -113};

    public static String encrypt(String plainText) {
        final SecretKeySpec keySpec = new SecretKeySpec(SECRET, ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new UnknownException(e);
        }
    }

    public static String decrypt(String cipherText) {
        final SecretKeySpec key = new SecretKeySpec(SECRET, ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new UnknownException(e);
        }
    }

    public static boolean isEncrypted(String data) {
        return !data.matches("[A-Za-z0-9+/=]+"); // Checks if Base64 encoded
    }

}
