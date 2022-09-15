package com.example.taskManager.utility;

public class Constant {
    final public static String PASSWORD_REGEX="^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[^A-Za-z0-9])"
            + "(?=\\S+$).{8,20}$";
    //final public static String EMAIL_REGEX = "[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";
    final public static String QUINBAY_EMAIL_REGEX="[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)*(?i)@quinbay.com$";
    final public static String SUCCESS = "Success";
    final public static String PASSWORD_PASSAGE="Password should at least be 8 characters long.\n"+
            "Password must contains atleast one lowercase character.\n"+
            "Password must contain atleast one uppercase character.\n"+
            "Password must contain atleast one number.\n"+
            "Password must contains atleast one special character.";

    //Email Validation
    public static final String EMAIL_ALREADY_EXISTS = "Email id is already associated with the application";
    public static final String EMAIL_NOT_EXISTS = "Email doesnot exists";
    public static final String INCORRECT_PASSWORD = "Incorrect Password!!!";
    public static final String EMAIL_PASSWORD_MATCH="Password and Email id Matches";
    public static final String CROSS_ORIGIN = "*";
    //Register Validation
    public static final String NOT_A_VALID_EMAIL = "Not a valid Email";
    public static final String EMAIL_EMPTY = "Email cannot be Empty";

    //Role Validation
    public static final String ROLE_EMPTY = "Role cannot be Empty";
    public static final String ROLE_EXCEPTION = "Role should either be an Intern or an Admin";

    //Name Exception
    public static final String NAME_REGEX = "^[A-Za-z][A-Za-z. ]{2,31}$";
    public static final String NAME_EMPTY = "Name cannot be empty.";
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 32;

    public static final String NAME_MINIMUM_LENGTH = "Length of the name must be greater than 2 Characters at least.";
    public static final String Name_MAXIMUM_LENGTH = "Length of the name must be lesser than 32 Characters.";
    public static final String NAME_INVALID_INPUT = "Invalid Name Input";

    //Controller
    public static final String REGISTER = "/register";
    public static final String USER ="/user";
    public static final String ALL_USER = "/allusers";
    public static final String DELETE_ALL = "/delete/all";
    public static final String LOGIN = "/login";
    public static final String REDIS_ALL_ELEMENT = "/redis/get/all";
    public static final String CHANGE_STATUS = "/status/change/{email}";

    //Authentication Response
    public static final String ERROR = "ERROR";
    public static final String OK = "OK";

    //Redis
    public static final String REDIS_EMAIL_PASSWORD = "MailPassword";
    public static final String USER_ROLE = "USER";
    public static final String NULL = null;

    //Mail Rest Template
    public static final String MAIL_SUBJECT = "Account Disabled.";
    public static final String MAIL_CONTENT = "Your account has been Disabled";
    public static final String REST_TEMPLATE_URL = "http://10.30.1.35:8081/products";
    public static final String GET_DETAILS ="/get/details";
}


