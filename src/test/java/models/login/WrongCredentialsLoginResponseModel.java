package models.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WrongCredentialsLoginResponseModel(
        String detail,
        String username,
        String password
) {}