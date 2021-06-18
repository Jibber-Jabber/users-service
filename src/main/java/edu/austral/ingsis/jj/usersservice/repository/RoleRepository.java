package edu.austral.ingsis.jj.usersservice.repository;

import edu.austral.ingsis.jj.usersservice.model.user.UserRole;
import edu.austral.ingsis.jj.usersservice.model.user.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRole, String> {
    Optional<UserRole> findByUserRoleType(UserRoleType userRoleType);
    Boolean existsByUserRoleType(UserRoleType userRoleType);
}
