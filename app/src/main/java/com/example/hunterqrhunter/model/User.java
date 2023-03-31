package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    String uid;
    String firstName;
    String lastName;
    String username;
    String email;
    int born;
    int hash;
    long totalScore;
    int highestUniqueQRScore;

    // Username and email validation variables
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9](?!.*([._])\\1)[a-zA-Z0-9._]{1,18}[a-zA-Z0-9]$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                                                "[a-zA-Z0-9_+&*-]+)*@" +
                                                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                                "A-Z]{2,7}$";
    private final Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);


    public User(String uid,String fn, String ln, int born, int hash) {
        this.uid = uid;
        this.firstName = fn;
        this.lastName = ln;
        this.born = born;
        this.hash = hash;
    }

    public User (String uid,String username, String email, long totalScore, int highestUniqueQRScore) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.totalScore = totalScore;
        this.highestUniqueQRScore = highestUniqueQRScore;
    }

    public User (String uid,String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }


//    Array<HashQR> qrArray;

    public String getFirstName() {
        return this.firstName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.uid);
        result.put("firstName", this.firstName);
        result.put("lastName", this.lastName);
        result.put("born", born);
        result.put("hash", hash);

        return result;
    }

    public int validateUserInfo() {
        Matcher usernameMatcher = usernamePattern.matcher(this.username);
        Matcher emailMatcher = emailPattern.matcher(this.email);

        if (this.username.length() < 3 || this.username.length() > 20) {
            return 1; // Exception 1: Username must be between 3 and 20 characters long.
        }

        if (!usernameMatcher.matches()) {
            if (!this.username.matches("^[a-zA-Z0-9].*")) {
                return 2; // Exception 2: Username must start with a number or letter.
            }
            if (!this.username.matches(".*[a-zA-Z0-9]$")) {
                return 3; // Exception 3: Username must end with a number or letter.
            }
            if (this.username.matches(".*([._])\\1.*")) {
                return 4; // Exception 4: Username must not contain repeating . or _ characters.
            }
            if (!this.username.matches("^[a-zA-Z0-9._]*$")) {
                return 5; // Exception 5: Username contains illegal characters.
            }
        }

        if (!emailMatcher.matches()) {
            return 6; // Exception 6: Invalid email
        }


        return 0;
    }

    public String getUid() {
        return uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public int getBorn() {
        return born;
    }

    public void setBorn(int born) {
        this.born = born;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }


}