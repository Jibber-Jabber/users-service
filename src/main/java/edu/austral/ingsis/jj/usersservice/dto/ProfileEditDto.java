package edu.austral.ingsis.jj.usersservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEditDto {

    @NotNull
    private String password;

    private String newPassword;

    @Email
    private String email;
    private String firstName;
    private String lastName;
}
