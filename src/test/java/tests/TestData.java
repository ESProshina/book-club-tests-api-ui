package tests;

import net.datafaker.Faker;

import java.util.UUID;

public class TestData {

    private static final Faker faker = new Faker();
    public static String generateUsername() {
        return "autotest_" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateNewUsername(String currentUsername) {
        return currentUsername + "_updated";
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static String generateWrongPassword() {
        return faker.internet().password();
    }

    public static String generateInvalidUsername() {
        return faker.name().fullName();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateBookTitle() {
        return "autotest_" + UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateBookAuthors() {
        return faker.book().author();
    }

    public static int generatePublicationYear() {
        return faker.number().numberBetween(1000, 2026);
    }

    public static String generateTelegramChatLink() {
        return "https://t.me/" + faker.name().firstName();
    }

    public static String generateDescription() {
        return faker.lorem().paragraph(5);
    }

    public static int generateAssessment() {
        return faker.number().numberBetween(1, 5);
    }

    public static int generateReadPages() {
        return faker.number().numberBetween(1, 100);
    }

    public static String generateReview() {
        return faker.lorem().sentence();
    }

    public static final String LOGIN_USERNAME = "Elena";
    public static final String LOGIN_PASSWORD = "123456";
    public static final String passwordDef = "123456";
    public static final String LOGIN_WRONG_USERNAME = "NonExistentUser123";
    public static final String LOGIN_WRONG_PASSWORD = "wrong_password";
    public static final String EMPTY_STRING = "";
    public static final String FIELD_REQUIRED_ERROR = "This field may not be blank.";
    public static final String EXPECTED_TOKEN_PATH = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_TOKEN_PREFIX = EXPECTED_TOKEN_PATH;  // алиас
    public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token_12345";
    public static final String INVALID_TOKEN_ERROR = "Token is invalid";
    public static final String EXPECTED_TOKEN_ERROR_CODE = "token_not_valid";
    public static final String INVALID_TOKEN_CODE = EXPECTED_TOKEN_ERROR_CODE;  // алиас
    public static final String EXPECTED_WRONG_TOKEN_DETAIL = "Token has wrong type";
    public static final String EXPECTED_BLOCKED_TOKEN_DETAIL = "Token is blacklisted";

    public static final String IP_ADDR_REGEXP =
            "^(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
    public static final String REGISTRATION_IP_REGEXP = IP_ADDR_REGEXP;  // алиас

    public static final String EXPECTED_EXISTING_USER_ERROR_MESSAGE =
            "A user with that username already exists.";
    public static final String REGISTRATION_EXISTING_USER_ERROR = EXPECTED_EXISTING_USER_ERROR_MESSAGE;  // алиас

    public static final String EXPECTED_INVALID_USERNAME_ERROR_MESSAGE =
            "Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.";

    public static final String EXPECTED_LOGIN_ERROR_DETAIL = "Invalid username or password.";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = EXPECTED_LOGIN_ERROR_DETAIL;  // алиас

    public static final String EXPECTED_USERNAME_ERROR = "This field is required.";
    public static final String EXPECTED_PASSWORD_ERROR = "This field is required.";

    public static final String UNAUTHORIZED_ERROR = "Authentication credentials were not provided.";

    public static final String UPDATED_FIRST_NAME = "Elena";
    public static final String UPDATED_LAST_NAME = "Black";
    public static final String UPDATED_EMAIL = "elena.black@example.com";
    public static final String INVALID_EMAIL = "invalid_email_format";
    public static final String INVALID_EMAIL_ERROR = "Enter a valid email address.";

    public static final String CLUB_BOOK_TITLE = "Test Book Title";
    public static final String CLUB_BOOK_AUTHORS = "Test Author";
    public static final Integer CLUB_PUBLICATION_YEAR = 2020;
    public static final String CLUB_DESCRIPTION = "Test club description";
    public static final String CLUB_TELEGRAM_LINK = "https://t.me/test_club";

    public static final String UPDATED_CLUB_BOOK_TITLE = "Updated Book Title";
    public static final String UPDATED_CLUB_BOOK_AUTHORS = "Updated Author";
    public static final Integer UPDATED_CLUB_PUBLICATION_YEAR = 2021;
    public static final String UPDATED_CLUB_DESCRIPTION = "Updated club description";
    public static final String UPDATED_CLUB_TELEGRAM_LINK = "https://t.me/updated_club";

    public static final int NON_EXISTENT_CLUB_ID = 999_999_999;
    public static final int nonExistentClubId = 999999;  // алиас

    public static final String EXPECTED_EXISTING_CLUB_ERROR_MESSAGE =
            "Book Club with this Book Title already exists.";
    public static final String notFoundError = "No Club matches the given query.";
    public static final String errorClub = "No Club matches the given query.";
    public static final String negativeYearError = "This field must be positive.";
    public static final String invalidTelegramLink = "invalid-url";

    public static final String REVIEW_TEXT = "Отличная книга, всем рекомендую!";
    public static final String UPDATED_REVIEW_TEXT = "Перечитал — ещё лучше!";
    public static final Integer REVIEW_ASSESSMENT = 5;
    public static final Integer REVIEW_READ_PAGES = 320;
    public static final Integer UPDATED_REVIEW_ASSESSMENT = 4;
    public static final Integer UPDATED_REVIEW_READ_PAGES = 400;

    public static final int NON_EXISTENT_REVIEW_ID = 999_999_999;

    public static final String errorBookReview = "No BookReview matches the given query.";
    public static final String EXPECTED_REVIEW_NOT_FOUND_ERROR_MESSAGE = "No BookReview matches the given query.";
    public static final String errorAssessment = "This field is required.";
    public static final String errorAssessmentRequired = "This field is required.";
    public static final String errorPermission = "You do not have permission to perform this action.";

    public static final String EXPECTED_INVALID_PAGE_ERROR_MESSAGE = "Invalid page.";
    public static final String EXPECTED_PROFILE_BTN_NAME = "Профиль";
    public static final String EXPECTED_CLUB_BTN_NAME = "Клубы";
    public static final String EXPECTED_CREATE_CLUB_BTN_NAME = "Создать клуб";

    public static final String EXPECTED_CANT_LEAVE_CLUB_FOR_OWNER_ERROR_MESSAGE = "Не удалось покинуть клуб";
    public static final String EXPECTED_MISMATCH_PASSWORD_ERROR_MESSAGE = "Пароли не совпадают";
    public static final String EXPECTED_SAME_CREDENTIALS_ERROR_MESSAGE = "Ошибка при регистрации";
    public static final String EXPECTED_WRONG_CREDENTIALS_ERROR_MESSAGE = "Ты не пройдешь!";
}