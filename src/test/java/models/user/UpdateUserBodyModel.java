package models.user;

public record UpdateUserBodyModel(
        String firstName,
        String lastName,
        String email
) {}