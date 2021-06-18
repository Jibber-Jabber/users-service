package edu.austral.ingsis.jj.usersservice;

import edu.austral.ingsis.jj.usersservice.dto.ProfileEditDto;
import edu.austral.ingsis.jj.usersservice.dto.RegisterRequestDto;
import edu.austral.ingsis.jj.usersservice.dto.UserDataDto;
import edu.austral.ingsis.jj.usersservice.exceptions.BadRequestException;
import edu.austral.ingsis.jj.usersservice.model.user.User;
import edu.austral.ingsis.jj.usersservice.repository.UserRepository;
import edu.austral.ingsis.jj.usersservice.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@MockBean
	PasswordEncoder encoder;

	@BeforeEach
	void setup(){
		when(encoder.encode(isA(String.class))).thenReturn("password");
	}

	@Test
	void Should_RegisterUser() {
		RegisterRequestDto register = new RegisterRequestDto("test", "password", "test@gmail.com", "name", "surname");
		userService.registerUser(register);
		Optional<User> user = userRepository.findByUsername("test");
		assertThat(user).isPresent();
	}

	@Test
	@WithTestUser(username = "test1")
	void Should_GetUserData(){
		UserDataDto data = userService.getUserData();
		assertThat(data.getUsername()).isEqualTo("test1");
	}

	@Test
	@WithTestUser(username = "test2", email = "test2@gmail.com")
	void Should_SearchUser() {
		RegisterRequestDto register = new RegisterRequestDto("test", "password", "test@gmail.com", "name", "surname");
		userService.registerUser(register);
		List<UserDataDto> users = userService.searchUserByUsername("test");
		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getUsername()).isEqualTo("test");
	}

	@Test
	@WithTestUser(username = "test3", email = "test3@gmail.com")
	void Should_EditProfile() {
		when(encoder.matches(isA(String.class), isA(String.class))).thenReturn(true);

		ProfileEditDto edit = ProfileEditDto.builder()
				.password("password")
				.email("new@gmail.com")
				.build();
		userService.editProfile(edit);

		Optional<User> user = userRepository.findByUsername("test3");
		assertThat(user).isPresent();
		assertThat(user.get().getEmail()).isEqualTo("new@gmail.com");
	}

	@Test
	@WithTestUser(username = "test4", email = "test4@gmail.com")
	void ShouldNot_EditProfile() {
		when(encoder.matches(isA(String.class), isA(String.class))).thenReturn(false);

		ProfileEditDto edit = ProfileEditDto.builder()
				.password("password")
				.email("new@gmail.com")
				.build();

		Throwable exception = assertThrows(BadRequestException.class, () -> userService.editProfile(edit));
		assertThat(exception.getMessage()).isEqualTo("Invalid Password");
	}


	@Test
	@WithTestUser(username = "test5", email = "test5@gmail.com")
	void Should_FollowAndUnfollowUser() {
		RegisterRequestDto register = new RegisterRequestDto("test", "password", "test@gmail.com", "name", "surname");
		User registered = userService.registerUser(register);

		userService.followUser(registered.getId());
		Optional<User> user = userRepository.findByUsername("test5");
		Optional<User> followed = userRepository.findByUsername("test");

		assertThat(user).isPresent();
		assertThat(followed).isPresent();

		assertThat(user.get().getFollowed().size()).isEqualTo(1);
		assertThat(user.get().getFollowed().contains(followed.get())).isTrue();

		assertThat(followed.get().getFollowers().size()).isEqualTo(1);
		assertThat(followed.get().getFollowers().contains(user.get())).isTrue();

		userService.unfollowUser(registered.getId());

		user = userRepository.findByUsername("test5");
		followed = userRepository.findByUsername("test");

		assertThat(user).isPresent();
		assertThat(followed).isPresent();

		assertThat(user.get().getFollowed()).isEmpty();
		assertThat(followed.get().getFollowers()).isEmpty();
	}
}
