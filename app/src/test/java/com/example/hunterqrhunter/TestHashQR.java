package com.example.hunterqrhunter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.hunterqrhunter.model.HashQR;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestHashQR {
    @Test
    public void testHashObject() {
        // Test with a simple string
        HashQR Hashing = new HashQR();
        String obj1 = "Hello World";
        byte[] hash1 = Hashing.hashObject(obj1);
        byte[] expectedHash1 = sha256(obj1.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expectedHash1, hash1);

        // Test with a null object
        String obj2 = null;
        byte[] hash2 = Hashing.hashObject(obj2);
        assertNull(hash2);
    }

    private byte[] sha256(byte[] data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testGiveQrName() {
        String hashVal = "dragonasf";
        HashQR Hashing = new HashQR();
        // Verify that the QR code name has the correct format
        byte[] hash = Hashing.hashObject(hashVal);
        String HashName = Hashing.giveQrName(hash);
        assertEquals(89, (int) hash[0]);
        assertEquals("PortJoyCallahan", HashName);
    }
}
