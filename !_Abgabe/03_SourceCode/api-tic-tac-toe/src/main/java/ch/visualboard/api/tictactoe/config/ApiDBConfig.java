package ch.visualboard.api.tictactoe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "api.database")
@Validated
public class ApiDBConfig
{
    private String driverClassName;
    private String hibernateDialect;
    private String hibernateDdlAuto;
    private String url;
    private String username;
    private String password;

    public String getDriverClassName()
    {
        return driverClassName;
    }

    public void setDriverClassName(final String driverClassName)
    {
        this.driverClassName = driverClassName;
    }

    public String getHibernateDialect()
    {
        return hibernateDialect;
    }

    public void setHibernateDialect(final String hibernateDialect)
    {
        this.hibernateDialect = hibernateDialect;
    }

    public String getHibernateDdlAuto()
    {
        return hibernateDdlAuto;
    }

    public void setHibernateDdlAuto(final String hibernateDdlAuto)
    {
        this.hibernateDdlAuto = hibernateDdlAuto;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }
}
