package shinhan.server_child.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class TransactionManagerConfig {

    @Bean(name = "commonTransactionManager")
    public PlatformTransactionManager commonTransactionManager(
            @Qualifier("commonDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "missionTransactionManager")
    public PlatformTransactionManager missionTransactionManagerB(
            @Qualifier("missionDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
