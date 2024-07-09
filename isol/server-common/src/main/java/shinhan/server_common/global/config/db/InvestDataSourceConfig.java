package shinhan.server_common.global.config.db;

import java.util.Objects;
import javax.sql.DataSource;
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

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@EnableJpaRepositories(
        basePackages = "shinhan.server_common.domain.invest.repository",
        entityManagerFactoryRef = "investEntityManagerFactory",
        transactionManagerRef = "investTransactionManager")
public class InvestDataSourceConfig {

    @Value("${INVEST_DB_URL}")
    private String dbUrl;
    @Value("${INVEST_DB_USERNAME}")
    private String dbUsername;
    @Value("${INVEST_DB_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource investDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean investEntityManagerFactory(
            @Qualifier("investDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("shinhan.server_common.domain.invest.entity");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager investTransactionManager(
            @Qualifier("investEntityManagerFactory") LocalContainerEntityManagerFactoryBean investEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(investEntityManagerFactory.getObject()));
    }
}
