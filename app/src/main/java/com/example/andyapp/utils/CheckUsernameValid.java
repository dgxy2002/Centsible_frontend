package com.example.andyapp.utils;

public class CheckUsernameValid implements UsernameChecker{
    public String validUsername(String username){
        if (isValidCharacters(username) && isValidLength(username)){
            return "valid";
        } else if (!isValidLength(username)){
            return "Username must be at least 3 characters long.";
        } else{
            return "Username can contain only letters and numbers.";
        }
    }
    private static boolean isValidLength(String password){
        return password.length() >= 3;
    }
    private static boolean isValidCharacters(String password){
        return password.matches("[A-Za-z0-9]+");
    }
}
