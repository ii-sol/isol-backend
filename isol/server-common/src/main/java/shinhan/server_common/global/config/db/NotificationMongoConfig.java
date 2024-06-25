package shinhan.server_common.global.config.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "shinhan.server_common.notification.mongo",
        mongoTemplateRef = "notificationMongoTemplate")
public class NotificationMongoConfig {

    @Value("${NOTIFICATION_DB_URI}")
    private String mongoUri;

    @Bean
    @Primary
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    @Primary
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), "notificationDatabase");
    }

    @Bean
    @Primary
    public MongoTemplate notificationMongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }
}
