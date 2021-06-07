package com.example.fooddelivery.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$");
    static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");

    public Regex(){};

    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    public boolean validatePassword(String passStr) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(passStr);
        return matcher.find();
    }
}
