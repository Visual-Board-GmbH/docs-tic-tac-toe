package ch.visualboard.api.tictactoe.config;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = {"ch.visualboard.api.tictactoe.dao", "ch.visualboard.api.tictactoe.repositories"},
    entityManagerFactoryRef = "apiTicTacToeEntityManager",
    transactionManagerRef = "apiTicTacToeTransactionManager"
)
public class DBConfig
{
    private final ApiDBConfig apiDBConfig;

    @Autowired
    public DBConfig(final ApiDBConfig apiDBConfig)
    {
        this.apiDBConfig = apiDBConfig;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean apiTicTacToeEntityManager()
    {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(apiAccessManagementDataSource());
        entityManager.setPackagesToScan("ch.visualboard.api.tictactoe.dao", "ch.visualboard.api.tictactoe.repositories");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", apiDBConfig.getHibernateDdlAuto());
        properties.put("hibernate.dialect", apiDBConfig.getHibernateDialect());
        properties.put("hibernate.ejb.entitymanager_factory_name", "apiTicTacToeEntityManager");

        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean
    @Primary
    public DataSource apiAccessManagementDataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(apiDBConfig.getDriverClassName());
        dataSource.setUrl(apiDBConfig.getUrl());
        dataSource.setUsername(apiDBConfig.getUsername());
        dataSource.setPassword(apiDBConfig.getPassword());

        return dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager apiTicTacToeTransactionManager()
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(apiTicTacToeEntityManager().getObject());
        return transactionManager;
    }
}
