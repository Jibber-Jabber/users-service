package edu.austral.ingsis.jj.usersservice.model.user;

import edu.austral.ingsis.jj.usersservice.model.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_role")
@Data
@NoArgsConstructor
public class UserRole extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;
}
