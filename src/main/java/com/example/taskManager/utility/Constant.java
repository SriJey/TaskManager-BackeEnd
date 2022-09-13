package com.example.taskManager.utility;

public class Constant {
    final public static String PASSWORD_REGEX="^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[^A-Za-z0-9])"
            + "(?=\\S+$).{8,20}$";
    final public static String EMAIL_REGEX = "[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";
    final public static String QUINBAY_EMAIL_REGEX="[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)*(?i)@quinbay.com$";
    final public static String SUCCESS = "Success";
    final public static String PASSWORD_PASSAGE="Password should at least be 8 characters long\n"+
            "Password must contains atleast one lowercase character.\n"+
            "Password must contain atleast one uppercase character.\n"+
            "Password must contain atleast one number.\n"+
            "Password must contains atleast one special character.";
}
