package org.develop.FunkoSpringJpa.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${api.version}")
    private String apiVersion;

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API REST - Funkos Spring DAW 2023-2024")
                                .version("1.0.0")
                                .description("API desarrollada para presentacion en clase de DAW - Desarrollada en Spring.")
                                .termsOfService("https://www.google.com/")
                                .license(
                                        new License()
                                                .name("CC BY-NC-SA 4.0")
                                                .url("https://www.google.com/")
                                )
                                .contact(
                                        new Contact()
                                                .name("Jorge Alonso Cruz Vera")
                                                .email("jorgealonso0876@gmail.com")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Repository and Documentation of the Project")
                                .url("https://github.com/Alonso2002-jpg/FunkoSpringJPAVista")
                )
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createSecurityScheme()));
    }

    @Bean
    GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("http")
                .pathsToMatch("/"+apiVersion+"/funkos/**", "/"+apiVersion+"/auth/**")
                .displayName("API Funkos Spring DAW 2023 - 2024")
                .build();
    }
}
