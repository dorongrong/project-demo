package lee.projectdemo;

import lee.projectdemo.item.aws.AwsS3Config;
import lee.projectdemo.item.config.QueryDslConfig;
import lee.projectdemo.login.config.SpringDataJpaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({SpringDataJpaConfig.class, AwsS3Config.class, QueryDslConfig.class})
@SpringBootApplication
public class ProjectDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectDemoApplication.class, args);
	}

}

