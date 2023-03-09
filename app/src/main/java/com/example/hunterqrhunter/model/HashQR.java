package com.example.hunterqrhunter.model;

public class HashQR {
    public int hash;
    public String HashName;
    //HashObject is a method that gets any type of object(QR code) returns hash code
    public static int hashObject(Object obj) {
        if (obj == null) {
            return 0;
        }
        int hash = obj.hashCode();
        if (hash == Integer.MIN_VALUE) {
            hash = 0;
        } else if (hash < 0) {
            hash = -hash;
        }
        return hash;
    }

    //NameGen() is a method that gets a hashed integer and generate a readable name out of that
    public static String nameGen(int hash) {
        // Define an array of syllables that can be used to generate the random name
        String[] syllables = {"a", "an", "ba", "be", "ca", "ce", "da", "de", "el", "en", "fa", "fe", "ga", "ge", "ha", "he", "ia", "ie", "ja", "je", "ka", "ke", "la", "le", "ma", "me", "na", "ne", "oa", "oe", "pa", "pe", "ra", "re", "sa", "se", "ta", "te", "ua", "ue", "va", "ve", "wa", "we", "xa", "xe", "ya", "ye", "za", "ze"};

        // Generate a random number to determine the length of the name
        int nameLength = (int) (Math.random() * 3) + 2; // generate a name with 2 to 4 syllables

        // Use a StringBuilder object to create the random name
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < nameLength; i++) {
            int index = (hash + i) % syllables.length;
            String syllable = syllables[index];
            // capitalize the first letter of the syllable to make it sound more like a name
            syllable = syllable.substring(0, 1).toUpperCase() + syllable.substring(1);
            builder.append(syllable);
        }
        String HashName = builder.toString();

        return HashName;
    }
}

