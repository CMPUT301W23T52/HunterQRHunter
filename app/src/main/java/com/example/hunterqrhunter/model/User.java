package com.example.hunterqrhunter.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a user of the app.
 */
public class User {
    String uid;
    String username;
    String email;
    int totalScore;
    ArrayList<String> scannedQRs;

    // Regex patterns used to validate username and email
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9](?!.*([._])\\1)[a-zA-Z0-9._]{1,18}[a-zA-Z0-9]$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    private final Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Creates a new user object with the given username, email, and unique identifier.
     * @param uid the unique identifier for the user
     * @param username the username for the user
     * @param email the email address for the user
     */
    public User (String uid,String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    /**
     * Creates a new user object with the given username, email, unique identifier, and total score.
     * @param username the username for the user
     * @param uid the unique identifier for the user
     * @param email the email address for the user
     * @param totalScore the total score for the user
     */
    public User(String username, String uid, String email, int totalScore) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.totalScore = totalScore;
    }

    /**
     * Validates the user's username and email.
     * @return 0 if validation succeeds, otherwise an integer indicating the type of validation error.
     */
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

    // Getters and setters

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public ArrayList<String> getScannedQRs() {
        return scannedQRs;
    }

    public void setScannedQRs(ArrayList<String> scannedQRs) {
        this.scannedQRs = scannedQRs;
    }



}