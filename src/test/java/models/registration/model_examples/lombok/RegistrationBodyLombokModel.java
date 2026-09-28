package models.registration.model_examples.lombok;

import lombok.Data;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class RegistrationBodyLombokModel {
    String username;
    String password;
}