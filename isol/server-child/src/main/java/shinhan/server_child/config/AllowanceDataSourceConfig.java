package shinhan.server_child.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
        basePackages = "shinhan.server_child.domain.allowance.repository",
        entityManagerFactoryRef = "allowanceEntityManagerFactory",
        transactionManagerRef = "allowanceTransactionManager")
public class AllowanceDataSourceConfig {
    @Value("${ALLOWANCE_DB_URL}")
    private String dbUrl;
    @Value("${ALLOWANCE_DB_USERNAME}")
    private String dbUsername;
    @Value("${ALLOWANCE_DB_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource allowanceDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean allowanceEntityManagerFactory(
            @Qualifier("allowanceDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
                "shinhan.server_child.domain.allowance.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager allowanceTransactionManager(
            @Qualifier("allowanceEntityManagerFactory") LocalContainerEntityManagerFactoryBean allowanceEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(allowanceEntityManagerFactory.getObject()));
    }
}
