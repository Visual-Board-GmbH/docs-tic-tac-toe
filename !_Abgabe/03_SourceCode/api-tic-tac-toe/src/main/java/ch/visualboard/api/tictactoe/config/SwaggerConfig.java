package ch.visualboard.api.tictactoe.config;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer
{
    private final ApiProjectConfig apiProjectConfig;

    @Autowired
    public SwaggerConfig(final ApiProjectConfig apiProjectConfig)
    {
        this.apiProjectConfig = apiProjectConfig;
    }

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("ch.visualboard.api.tictactoe.resources"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(createApiInfo());
    }

    private ApiInfo createApiInfo()
    {
        return new ApiInfo(
            "Tic-Tac-Toe API",
            "Interface for Tic Tac Toe Game",
            "v1: " + apiProjectConfig.getVersion(),
            "",
            new Contact("Visual Board GmbH", "https://abbts.ch", "dbn-gruppe1@protonmail.com"),
            "",
            "",
            new ArrayList<>()
        );
    }
}
