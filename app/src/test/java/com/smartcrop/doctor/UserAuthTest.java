package com.smartcrop.doctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.smartcrop.doctor.auth.PasswordUtils;
import com.smartcrop.doctor.database.entity.User;

import org.junit.Test;

public class UserAuthTest {

    @Test
    public void testSaltGeneration() {
        String salt1 = PasswordUtils.generateSalt();
        String salt2 = PasswordUtils.generateSalt();

        assertNotNull(salt1);
        assertNotNull(salt2);
        assertFalse(salt1.isEmpty());
        assertNotEquals("Each generated salt must be uniquely random", salt1, salt2);
    }

    @Test
    public void testPasswordHashingAndVerification() {
        String salt = PasswordUtils.generateSalt();
        String password = "FarmerSecurePassword123!";

        String hash = PasswordUtils.hashPassword(password, salt);
        assertNotNull(hash);
        assertFalse(hash.isEmpty());

        // Correct password matches
        assertTrue("Verification should succeed with correct credentials",
                PasswordUtils.verifyPassword(password, salt, hash));

        // Wrong password fails
        assertFalse("Verification must fail with incorrect password",
                PasswordUtils.verifyPassword("WrongPassword", salt, hash));

        // Tampered hash fails
        assertFalse("Verification must fail with tampered hash",
                PasswordUtils.verifyPassword(password, salt, "tampered_hash_value"));
    }

    @Test
    public void testUserEntityAttributes() {
        long now = System.currentTimeMillis();
        String salt = PasswordUtils.generateSalt();
        String hash = PasswordUtils.hashPassword("secret123", salt);

        User farmer = new User("Jane Doe", "jane@greenfarm.org", "Sunrise Organic Farm", hash, salt, now, now);
        assertEquals("Jane Doe", farmer.getFullName());
        assertEquals("jane@greenfarm.org", farmer.getEmailOrPhone());
        assertEquals("Sunrise Organic Farm", farmer.getFarmName());
        assertEquals(hash, farmer.getPasswordHash());
        assertEquals(salt, farmer.getSalt());

        farmer.setFullName("Jane Smith");
        assertEquals("Jane Smith", farmer.getFullName());
    }
}
