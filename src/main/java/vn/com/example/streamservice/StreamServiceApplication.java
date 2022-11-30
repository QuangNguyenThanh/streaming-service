package vn.com.example.streamservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import vn.com.example.streamservice.config.StreamProperties;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties({ StreamProperties.class})
@EnableJpaRepositories
public class StreamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamServiceApplication.class, args);
	}

}
