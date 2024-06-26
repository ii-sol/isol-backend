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
                "shinhan.server_common.domain.invest.repository"},
        entityManagerFactoryRef = "commonEntityManagerFactory",
        transactionManagerRef = "commonTransactionManager")
public class CommonDataSourceConfig {

    @Value("${COMMON_DB_URL}")
    private String dbUrl;
    @Value("${COMMON_DB_USERNAME}")
    private String dbUsername;
    @Value("${COMMON_DB_PASSWORD}")
    private String dbPassword;

    @Bean
    public DataSource commonDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean commonEntityManagerFactory(
            @Qualifier("commonDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
                "shinhan.server_common.domain.account.entity",
                "shinhan.server_common.domain.invest.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager commonTransactionManager(
            @Qualifier("commonEntityManagerFactory") LocalContainerEntityManagerFactoryBean commonEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(commonEntityManagerFactory.getObject()));
    }
}
