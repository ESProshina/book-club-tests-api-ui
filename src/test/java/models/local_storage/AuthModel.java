package models.local_storage;

public record AuthModel(
        UserLocalStorageModel user,
        String accessToken,
        String refreshToken,
        boolean isAuthenticated
) {}