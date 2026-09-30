package models.local_storage;

public record UserLocalStorageModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr
) {}