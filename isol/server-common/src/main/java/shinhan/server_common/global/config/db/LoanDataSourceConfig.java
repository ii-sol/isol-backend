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
        basePackages = "shinhan.server_common.domain.loan.repository",
        entityManagerFactoryRef = "loanEntityManagerFactory",
        transactionManagerRef = "loanTransactionManager")
public class LoanDataSourceConfig {

    @Value("${LOAN_DB_URL}")
    private String dbUrl;
    @Value("${LOAN_DB_USERNAME}")
    private String dbUsername;
    @Value("${LOAN_DB_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource loanDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean loanEntityManagerFactory(
            @Qualifier("loanDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("shinhan.server_common.domain.loan.entity");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager loanTransactionManager(
            @Qualifier("loanEntityManagerFactory") LocalContainerEntityManagerFactoryBean loanEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(loanEntityManagerFactory.getObject()));
    }
}
