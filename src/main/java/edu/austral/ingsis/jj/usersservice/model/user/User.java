package edu.austral.ingsis.jj.usersservice.model.user;

import edu.austral.ingsis.jj.usersservice.model.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"followed", "followers"})
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

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> followed;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> followers;

    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRole role;

}
