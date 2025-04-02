package com.example.andyapp.utils;

public class CheckPasswordValid {
    public static String validPassword(String password){
        if (isValidLength(password) & isValidCharacters(password)){
            return "valid";
        }else{
            if (!isValidLength(password)){
                return "Password needs to be at least 6 digits long.";
            } else {
                return "Password can only contain letters and numbers.";
            }
        }
    }

    private static boolean isValidLength(String password){
        return password.length() >=7;
    }
    private static boolean isValidCharacters(String password){
        return password.matches("[A-Za-z0-9]+");
    }
}
