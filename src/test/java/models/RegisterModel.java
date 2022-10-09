package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RegisterModel {

    private String email;
    private String password;
}
