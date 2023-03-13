package com.example.hunterqrhunter.model;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

    //giveQrName() is a method that gets a hashed integer and generate a readable name out of that
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

    //createQrCreature() method creates Qr code image with bitmap
    public Bitmap generateImageFromHashcode(int hashcode) {
        // Default size of the image
        int width = 350;
        int height = 350;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set a random color based on the hashcode
        int red = (hashcode & 0xFF0000) >> 16;
        int green = (hashcode & 0x00FF00) >> 8;
        int blue = hashcode & 0x0000FF;
        int color = Color.rgb(red, green, blue);

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

        paint.setColor(Color.BLACK);
        canvas.drawCircle(leftEyeCenterX, leftEyeCenterY, eyeRadius / 2f, paint);
        canvas.drawCircle(rightEyeCenterX, rightEyeCenterY, eyeRadius / 2f, paint);

        // Draw the mouth
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

