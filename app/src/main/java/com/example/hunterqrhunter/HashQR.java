package com.example.hunterqrhunter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashQR {
    public int hash;
    public String HashName;
    //HashObject is a method that gets any type of object(QR code) returns hash code
    public static byte[] hashObject(String obj) {
        if (obj == null) {
            return null;
        }
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] data = new byte[0];
        try {
            data = obj.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        byte[] hash = messageDigest.digest(data);
        return hash;
    }

    //NameGen() is a method that gets a hash value and generates a readable name out of that
    public static String nameGen(byte[] hash) {
        // Define an array of syllables that can be used to generate the random name
        String[] syllables = {"a", "an", "ba", "be", "ca", "ce", "da", "de", "el", "en", "fa", "fe", "ga", "ge", "ha", "he", "ia", "ie", "ja", "je", "ka", "ke", "la", "le", "ma", "me", "na", "ne", "oa", "oe", "pa", "pe", "ra", "re", "sa", "se", "ta", "te", "ua", "ue", "va", "ve", "wa", "we", "xa", "xe", "ya", "ye", "za", "ze"};

        // Generate a random number to determine the length of the name
        int nameLength = (int) (Math.random() * 3) + 2; // generate a name with 2 to 4 syllables

        // Use a StringBuilder object to create the random name
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < nameLength; i++) {
            int index = (Byte.toUnsignedInt(hash[i]) + i) % syllables.length;
            String syllable = syllables[index];
            // capitalize the first letter of the syllable to make it sound more like a name
            syllable = syllable.substring(0, 1).toUpperCase() + syllable.substring(1);
            builder.append(syllable);
        }
        String HashName = builder.toString();

        return HashName;
    }

    // scoreGen() is a method that takes in a hash value and generates an appropriate score
    public static int scoreGen(byte[] hash) {
        byte lastByte = hash[hash.length - 1];
        int repeatCount = 0;
        int score = 0;

        for (int i = 0; i < hash.length; i++) {
            if (hash[i] == lastByte) {
                repeatCount++;
                if (i == hash.length - 1) {
                    score += Byte.toUnsignedInt(lastByte)^repeatCount;
                }
                if (lastByte == 0) {
                    score += 20^(repeatCount - 1);
                }
            } else {
                score += Byte.toUnsignedInt(lastByte)^repeatCount;
                if (lastByte == 0) {
                    score += 20^(repeatCount - 1);
                }
                repeatCount = 0;
            }
            lastByte = hash[i];
        }

        return score;
    }
}

