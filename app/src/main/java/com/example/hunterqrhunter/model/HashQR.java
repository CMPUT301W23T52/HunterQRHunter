package com.example.hunterqrhunter.model;


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
//    public Bitmap generateImageFromHashcode(int hashcode) {
//
//        //default size of the image
//        int width = 350;
//        int height = 350;
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//
//        // Set a random color based on the hashcode
//        int red = (hashcode & 0xFF0000) >> 16;
//        int green = (hashcode & 0x00FF00) >> 8;
//        int blue = hashcode & 0x0000FF;
//        int color = Color.rgb(red, green, blue);
//
//        // Draw a rectangle with the random color
//        Paint paint = new Paint();
//        paint.setColor(color);
//        canvas.drawRect(0, 0, width, height, paint);
//
////        int[] pixels = new int[width * height];
////
////        bitmap.getPixels(pixels, 0, width, 0, 0 , width, height);
////
////        // Return the generated image
////        List<Integer> bitmapList = new ArrayList<Integer>();
////        for (int i = 0; i < pixels.length; i++) {
////            bitmapList.add(pixels[i]);
////        }
//        return bitmap;
//    }


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

