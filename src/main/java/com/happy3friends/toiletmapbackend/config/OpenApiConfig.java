package com.happy3friends.toiletmapbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI toiletMapOpenApi() {
        Server productionServer = new Server();
        productionServer.setUrl("https://toiletmap.azurewebsites.net/");
        productionServer.setDescription("Server URL in Production environment");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Server URL in Local environment");

        return new OpenAPI()
                .info(new Info().title("TOILET MAP API")
                        .description("Toilet Map API")
                        .contact(new Contact()
                                .email("tien.huynhlt.tn@gmail.com")
                                .name("Tien Huynh TN")
                                .url("https://github.com/tienhuynh-tn"))
                        .license(new License()
                                .name("The GNU General Public License v3.0")
                                .url("https://www.gnu.org/licenses/gpl-3.0.html"))
                        .version("1.0.0"))
                .servers(List.of(productionServer, localServer))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi userOpenApi() {
        String paths[] = {
                "/api/",
                "/api/users/**"
        };
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi managerOpenApi() {
        String paths[] = {
                "/api/toilets/**"
        };
        return GroupedOpenApi.builder()
                .group("Manager")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi applicationOpenApi() {
        return GroupedOpenApi.builder()
                .group("API Toilet Map v1.0.0")
                .pathsToMatch("/api/**")
                .build();
    }
}
