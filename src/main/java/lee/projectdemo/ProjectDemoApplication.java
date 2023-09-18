package lee.projectdemo;

import lee.projectdemo.login.config.SpringDataJpaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(SpringDataJpaConfig.class)
//@SpringBootApplication()
public class ProjectDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectDemoApplication.class, args);
	}

}

