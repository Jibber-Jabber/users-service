package edu.austral.ingsis.jj.usersservice.model.user;

import edu.austral.ingsis.jj.usersservice.model.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_data")
@Data
@NoArgsConstructor
public class User extends AbstractEntity {

    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRole role;

}
