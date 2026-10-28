package com.smartcrop.doctor.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility for hashing and verifying passwords securely for offline farmer accounts.
 * Uses SHA-256 and cryptographic salts with pure Java hex encoding to guarantee
 * full compatibility across all Android API levels and JVM unit tests.
 */
public class PasswordUtils {

    private static final int SALT_BYTES = 16;
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    /**
     * Generate a cryptographically secure random salt in hexadecimal.
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * Hash a plaintext password with the provided salt using SHA-256.
     */
    public static String hashPassword(String password, String salt) {
        if (password == null) {
            password = "";
        }
        if (salt == null) {
            salt = "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verify whether a plaintext password matches the stored salted hash.
     */
    public static boolean verifyPassword(String password, String salt, String expectedHash) {
        if (password == null || salt == null || expectedHash == null) {
            return false;
        }
        String calculated = hashPassword(password, salt);
        return calculated.equalsIgnoreCase(expectedHash);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARS[v >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }
}
