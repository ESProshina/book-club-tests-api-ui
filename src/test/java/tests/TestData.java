package tests;

public class TestData {

    public static final String LOGIN_USERNAME = "Elena";
    public static final String LOGIN_PASSWORD = "123456";

    public static final String LOGIN_WRONG_USERNAME = "NonExistentUser123";
    public static final String LOGIN_WRONG_PASSWORD = "wrong_password";
    public static final String EMPTY_STRING = "";
    public static final String FIELD_REQUIRED_ERROR = "This field may not be blank.";

    public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token_12345";
    public static final String INVALID_TOKEN_ERROR = "Token is invalid";
    public static final String INVALID_TOKEN_CODE = "token_not_valid";

    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    public static final String UPDATED_FIRST_NAME = "Elena";
    public static final String UPDATED_LAST_NAME = "Black";
    public static final String UPDATED_EMAIL = "elena.black@example.com";
    public static final String UPDATED_FULL_NAME = "Elena Black";
    public static final String INVALID_EMAIL = "invalid_email_format";

    public static final String INVALID_EMAIL_ERROR = "Enter a valid email address.";
    public static final String UNAUTHORIZED_ERROR = "Authentication credentials were not provided.";

    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";
    public static final String REGISTRATION_EXISTING_USER_ERROR = "A user with that username already exists.";

    // ==================== КЛУБЫ (CRUD) ====================

    public static final String CLUB_BOOK_TITLE = "Test Book Title";
    public static final String CLUB_BOOK_AUTHORS = "Test Author";
    public static final Integer CLUB_PUBLICATION_YEAR = 2020;
    public static final String CLUB_DESCRIPTION = "Test club description";
    public static final String CLUB_TELEGRAM_LINK = "https://t.me/test_club";

    public static final String UPDATED_CLUB_BOOK_TITLE = "Updated Book Title";
    public static final String UPDATED_CLUB_BOOK_AUTHORS = "Updated Author";
    public static final Integer UPDATED_CLUB_PUBLICATION_YEAR = 2021;
    public static final String UPDATED_CLUB_DESCRIPTION = "Updated description";
    public static final String UPDATED_CLUB_TELEGRAM_LINK = "https://t.me/updated_club";

    public static final int NON_EXISTENT_CLUB_ID = 999_999_999;

    // ==================== РЕВЬЮ ====================

    public static final String REVIEW_TEXT = "Отличная книга, всем рекомендую!";
    public static final String UPDATED_REVIEW_TEXT = "Перечитал — ещё лучше!";
    public static final Integer REVIEW_ASSESSMENT = 5;
    public static final Integer REVIEW_READ_PAGES = 320;
    public static final Integer UPDATED_REVIEW_ASSESSMENT = 4;
    public static final Integer UPDATED_REVIEW_READ_PAGES = 400;

    public static final int NON_EXISTENT_REVIEW_ID = 999_999_999;
}