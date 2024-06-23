package com.example.middleware.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

        @Bean
        public OpenAPI defineOpenApi() {
            Server server = new Server();
            server.setUrl("http://localhost:8080");
            server.setDescription("Development");

            Contact myContact = new Contact();
            myContact.setName("Bruno Pavlovic");
            myContact.setEmail("bruno.pavlovic66@gmail.com");

            SecurityScheme scheme = new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("Authorization");

            Info information = new Info()
                    .title("Product Middleware API")
                    .version("1.0")
                    .description("This API exposes endpoints that are working with data from dummyjson.com and H2 database.")
                    .contact(myContact);
            return new OpenAPI().info(information)
                    .servers(List.of(server))
                    .components(new Components().addSecuritySchemes("bearer-key", scheme))
                    .addSecurityItem(new SecurityRequirement().addList("bearer-key", Arrays.asList("read", "write")));
        }
}
