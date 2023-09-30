package lee.projectdemo;

import lee.projectdemo.login.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootTest
class ProjectDemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Bean
	@Profile("test")
	public TestDataInit testDataInit(UserRepository userRepository) {
		return new TestDataInit(userRepository);
	}

}
