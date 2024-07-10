package shinhan.server_common.global.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
@EnableJpaRepositories(
        basePackages = {
                "shinhan.server_common.domain.account.repository",
                "shinhan.server_common.domain.stock.repository"},
        entityManagerFactoryRef = "externalEntityManagerFactory",
        transactionManagerRef = "externalTransactionManager")
public class ExternalDataSourceConfig {

    @Value("${EXTERNAL_DB_URL}")
    private String dbUrl;
    @Value("${EXTERNAL_DB_USERNAME}")
    private String dbUsername;
    @Value("${EXTERNAL_DB_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource externalDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean externalEntityManagerFactory(
            @Qualifier("externalDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
                "shinhan.server_common.domain.account.entity",
                "shinhan.server_common.domain.stock.entity");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager externalTransactionManager(
            @Qualifier("externalEntityManagerFactory") LocalContainerEntityManagerFactoryBean externalEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(externalEntityManagerFactory.getObject()));
    }
}
