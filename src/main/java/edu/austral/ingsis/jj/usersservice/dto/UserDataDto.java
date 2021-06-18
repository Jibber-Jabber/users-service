package edu.austral.ingsis.jj.usersservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.austral.ingsis.jj.usersservice.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataDto {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private String role;

    public static UserDataDto from(User user){
        return UserDataDto.builder()
                .email(user.getEmail())
                .userId(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().getUserRoleType().name())
                .build();
    }
}
