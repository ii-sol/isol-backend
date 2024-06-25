package shinhan.server_child.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@EnableJpaRepositories(
        basePackages = "shinhan.server_child.domain.mission.repository",
        entityManagerFactoryRef = "missionEntityManagerFactory",
        transactionManagerRef = "missionTransactionManager")
public class MissionDataSourceConfig {

    @Value("${MISSION_DB_URL}")
    private String dbUrl;
    @Value("${MISSION_DB_USERNAME}")
    private String dbUsername;
    @Value("${MISSION_DB_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource missionDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean missionEntityManagerFactory(
            @Qualifier("missionDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
                "shinhan.server_common.domain.mission.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager missionTransactionManager(
            @Qualifier("missionEntityManagerFactory") LocalContainerEntityManagerFactoryBean missionEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(missionEntityManagerFactory.getObject()));
    }
}
