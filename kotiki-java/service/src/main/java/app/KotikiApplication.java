package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"controller", "service", "app"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = {"model", "dto"})
public class KotikiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KotikiApplication.class, args);
    }
}
