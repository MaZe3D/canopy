package net.canopy.filters.builtin.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import net.canopy.app.api.CanopyException;

public class AesCryptor {
    private SecretKeySpec secretKeySpec;
    public AesCryptor(String password) throws CanopyException{
        this.secretKeySpec = this.deriveKeyFromPassword(password);
    }

    private SecretKeySpec deriveKeyFromPassword(String password) throws CanopyException {
        byte[] salt = "w8ZL5FN1sR".getBytes(StandardCharsets.UTF_8);
        int iterations = 10000;
        int keyLength = 128;

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        }
        catch (Throwable e) {
            throw new CanopyException(this.getClass().getName() + ".deriveKeyFromPassword(String)", "Unable to derive key from password. " + e.getMessage());
        }
    }

    // convert values to strings, encrypt the strings, apply bas64 encoding
    public String encrypt(String plaintext) throws CanopyException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Throwable e) {
            throw new CanopyException(this.getClass().getName() + ".encrypt(String)", "Unable to encrypt string. " + e.getMessage());
        }
    }

    // decode bas64 + decrypt string
    public String decrypt(String ciphertext) throws CanopyException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec);
            byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }
        catch (Throwable e) {
            throw new CanopyException(this.getClass().getName() + ".decrypt(String)", "Unable to decrypt string. " + e.getMessage());
        }
    }
}
