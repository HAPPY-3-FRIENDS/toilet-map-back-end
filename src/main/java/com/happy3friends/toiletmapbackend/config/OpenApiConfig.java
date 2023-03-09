package com.happy3friends.toiletmapbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI toiletMapOpenApi() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Server URL in Local environment");

        /*Server productionServer = new Server();
        productionServer.setUrl("https://my-awesome-api.com");
        productionServer.setDescription("Server URL in Production environment");*/

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
                        .servers(List.of(localServer/*, productionServer*/));
    }
}
