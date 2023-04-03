package com.example.hunterqrhunter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.hunterqrhunter.model.User;

public class UserUnitTest {

    @Test
    public void validateUserInfo_valid() {
        User user = new User("testuid", "validUsername", "validUsername@example.com");
        assertEquals(0, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_username_too_short() {
        User user = new User("TestID", "ab", "ab@example.com");
        assertEquals(1, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_username_too_long() {
        User user = new User("TestID", "abcdefghijklmnopqrst1", "abcdefghijklmnopqrst1@example.com");
        assertEquals(1, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_username_starts_with_invalid_character() {
        User user = new User("TestID", ".invalidUsername", "validUsername@example.com");
        assertEquals(2, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_username_ends_with_invalid_character() {
        User user = new User("TestID", "invalidUsername_", "validUsername@example.com");
        assertEquals(3, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_username_has_repeating() {
        User user = new User("TestID", "repeat..invalid", "validUsername@example.com");
        assertEquals(4, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_username_contains_illegal_character() {
        User user = new User("TestID", "invalid@Username", "validUsername@example.com");
        assertEquals(5, user.validateUserInfo());
    }

    @Test
    public void validateUserInfo_invalid_email() {
        User user = new User("TestID", "validUsername", "validUsername@example");
        assertEquals(6, user.validateUserInfo());
    }

}
