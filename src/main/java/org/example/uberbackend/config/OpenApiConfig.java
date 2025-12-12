package org.example.uberbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI uberBackendOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("UberBackend API Support");
        contact.setEmail("support@uberbackend.com");

        License license = new License();
        license.setName("Educational Project License");
        license.setUrl("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("UberBackend API Documentation")
                .version("1.0.0")
                .description(
                    "Complete REST API documentation for UberBackend - A ride-sharing system built with Spring Boot.\n\n" +
                    "**Features:**\n" +
                    "- JWT Authentication & Authorization\n" +
                    "- Role-Based Access Control (Passengers & Drivers)\n" +
                    "- Core Ride Operations (Create, Accept, Complete)\n" +
                    "- Advanced Query Operations (10+ query APIs with MongoTemplate)\n" +
                    "- Analytics Dashboard (MongoDB Aggregation Pipelines)\n" +
                    "- Health Monitoring\n\n" +
                    "**Total APIs:** 23 REST Endpoints\n\n" +
                    "**How to Use:**\n" +
                    "1. Register a user via `/api/auth/register` with role ROLE_USER (passenger) or ROLE_DRIVER (driver)\n" +
                    "2. Login via `/api/auth/login` to get JWT token\n" +
                    "3. Click 'Authorize' button (bottom right) and enter: `Bearer <your-token>`\n" +
                    "4. Test any API endpoint directly from this UI"
                )
                .contact(contact)
                .license(license);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("Enter JWT token obtained from /api/auth/login endpoint. Format: Bearer <token>");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}

