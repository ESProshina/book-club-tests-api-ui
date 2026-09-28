//package models.registration;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//public record RegistrationBodyModel(
//        @JsonProperty("username") String username,
//        @JsonProperty("password") String password,
//        @JsonProperty("email") String email,
//        @JsonProperty("first_name") String firstName,
//        @JsonProperty("last_name") String lastName
//) {}

package models.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegistrationBodyModel(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("email") String email,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName
) {
    // Конструктор для удобства с 2 параметрами (username и password)
    // Остальные поля будут пустыми
    public RegistrationBodyModel(String username, String password) {
        this(username, password, "", "", "");
    }
}