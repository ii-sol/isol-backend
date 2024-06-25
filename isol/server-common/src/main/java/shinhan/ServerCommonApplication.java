package shinhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// common
@SpringBootApplication
@EnableJpaRepositories
public class ServerCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerCommonApplication.class, args);
    }
}