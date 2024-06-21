package shinhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import shinhan.server_common.notification.mongo.NotificationRepository;

// common
@SpringBootApplication
//@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
//    type = FilterType.ASSIGNABLE_TYPE,
//    classes = NotificationRepository.class))
//@EnableMongoRepositories(basePackages = {"shinhan.server_common.notification.mongo"})
public class ServerCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerCommonApplication.class, args);
    }
}