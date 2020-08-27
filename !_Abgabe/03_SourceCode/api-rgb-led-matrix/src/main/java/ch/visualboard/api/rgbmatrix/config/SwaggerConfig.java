package ch.visualboard.api.rgbmatrix.config;

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
            .apis(RequestHandlerSelectors.basePackage("ch.visualboard.api.rgbmatrix.resources"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(createApiInfo());
    }

    private ApiInfo createApiInfo()
    {
        return new ApiInfo(
            "RGB Matrix API",
            "Interface for LED RGB Panel over FPGA",
            "v1: " + apiProjectConfig.getVersion(),
            "",
            new Contact("Visual Board GmbH", "https://abbts.ch", "samuel.j.salomon@gmail.com"),
            "",
            "",
            new ArrayList<>()
        );
    }
}
