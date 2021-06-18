package edu.austral.ingsis.jj.usersservice.model.user;

import edu.austral.ingsis.jj.usersservice.model.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> followed;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> followers;

    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRole role;

    public User(@NotNull String username, @NotNull @Email String email, @NotNull String password, @NotNull String firstName, @NotNull String lastName, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        followers = new HashSet<>();
        followed = new HashSet<>();
    }
}
