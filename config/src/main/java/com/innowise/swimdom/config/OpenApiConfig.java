package com.innowise.swimdom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Config OpenApi.
 */
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    private final BuildProperties buildProperties;

    /**
     * Bean OpenApi.
     *
     * @return configured OpenApi
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ORS service.")
                .version(buildProperties.getVersion())
                .description(
                    "Service for buying ticket swimming pool."));
    }
}
