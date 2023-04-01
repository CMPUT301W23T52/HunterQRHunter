package com.example.hunterqrhunter.model;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashQR {
    public int hash;
    public String HashName;
    /**
     * Computes the SHA-256 hash of a given string object and returns the resulting hash code as a byte array. If the input
     * object is null, the method returns null.
     *
     * @param obj the string object to be hashed
     * @return a byte array representing the SHA-256 hash code of the input object, or null if the input object is null
     * @throws RuntimeException if the SHA-256 algorithm is not available or if the UTF-8 character set is not supported
     */
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
    /**
     * Generates a random name based on a given hashcode, using an array of syllables to create the name. The length of
     * the name is determined randomly by generating a number between 2 and 3. The first letter of each syllable is
     * capitalized to make the resulting name sound more like a proper name.
     *
     * @param hash a byte array representing a hashcode used to generate the random name
     * @return a string representing a randomly generated name based on the hashcode
     */
    public static String giveQrName(byte[] hash) {
        // Define an array of syllables that can be used to generate the random name
        String[] syllables = {"cooper", "diana", "charlotte", "jane", "watson", "kensington", "elise", "alexander", "ra", "joy", "callahan", "george", "william", "lemon", "bob", "mo", "lee", "port"};

        // Generate a random number to determine the length of the name
        int nameLength = (int) Byte.toUnsignedInt(hash[0]) % 2 + 2; // generate a name with 2 to 3 syllables

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
    /**
     * Generates a bitmap image of a face based on a given hashcode. The face will have a randomly generated color
     * based on the hashcode, and the color of the eyes and mouth will also change based on the hashcode.
     *
     * @param facecode an integer hashcode used to generate the face image
     * @return a bitmap image of a face with a randomly generated color and eye/mouth colors based on the hashcode
     */
    public static Bitmap generateImageFromHashcode(byte[] facecode) {
        // Default size of the image
        int width = 350;
        int height = 350;

        int hashcode = Arrays.hashCode(facecode);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set a random color based on the hashcode
        int red = (hashcode & 0xFF0000) >> 16;
        int green = (hashcode & 0x00FF00) >> 8;
        int blue = hashcode & 0x0000FF;
        int color = Color.rgb(red, green, blue);
        int eyeColor = Color.rgb((hashcode & 0xFF0000) >> 16, (hashcode & 0x00FF00) >> 8, hashcode & 0x0000FF);
        int mouthColor = Color.rgb((hashcode & 0x00FF00) >> 8, hashcode & 0xFF, (hashcode & 0xFF0000) >> 16);


        // Draw the face using the random color
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        // Draw the face shape
        float centerX = width / 2f;
        float centerY = height / 2f;
        float radius = width / 3f;

        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw the eyes
        float eyeOffsetX = radius / 3f;
        float eyeOffsetY = radius / 3f;

        float leftEyeCenterX = centerX - eyeOffsetX;
        float leftEyeCenterY = centerY - eyeOffsetY;
        float rightEyeCenterX = centerX + eyeOffsetX;
        float rightEyeCenterY = centerY - eyeOffsetY;
        float eyeRadius = radius / 6f;

        paint.setColor(Color.WHITE);
        canvas.drawCircle(leftEyeCenterX, leftEyeCenterY, eyeRadius, paint);
        canvas.drawCircle(rightEyeCenterX, rightEyeCenterY, eyeRadius, paint);

        paint.setColor(eyeColor);
        canvas.drawCircle(leftEyeCenterX, leftEyeCenterY, eyeRadius / 2f, paint);
        canvas.drawCircle(rightEyeCenterX, rightEyeCenterY, eyeRadius / 2f, paint);

        // Draw the mouth
        paint.setColor(mouthColor);
        float mouthOffsetY = radius / 3f;
        float mouthWidth = radius / 2f;
        float mouthHeight = radius / 4f;
        float mouthLeft = centerX - mouthWidth / 2f;
        float mouthTop = centerY + mouthOffsetY;
        float mouthRight = centerX + mouthWidth / 2f;
        float mouthBottom = mouthTop + mouthHeight;

        canvas.drawRect(mouthLeft, mouthTop, mouthRight, mouthBottom, paint);

        return bitmap;
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

