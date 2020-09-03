package demo.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import sun.security.acl.PrincipalImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

import java.security.Principal;

@RunWith(SpringRunner.class)
public class UserServiceTest {
	 private static final long ID_USER = 1l;

	@MockBean
	 private UserRepository userRepository;

	 private UserService userService;
	 
	 @Before
	 public void setup() {
		 userService = new UserService(userRepository);
	 }
	 
	 @Test
	 public void getUserGivenPrincipalShouldReturnUser() {
		 User expectedUser = new User();
		 expectedUser.setId(ID_USER);
		 given(userRepository.findUserByUsername("user")).willReturn(expectedUser);
		 User user = userService.getUserByPrincipal(new PrincipalImpl("user"));
		 assertThat(user.getId()).isEqualTo(ID_USER);
	 }
	 
	 @Test
	 public void getUserNoPrincipalShouldReturnNull() {
		 User user = userService.getUserByPrincipal(null);
		assertThat(user).isNull();
	 }
	 

}
