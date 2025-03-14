package com.mamydinyah.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Mamy Dinyah",
                        email = "mamydinyah34@gmail.com"
                ),
                title = "USER MANAGEMENT API",
                version = "1.0",
                description = "User Management API Documentation"
        ),
        servers = @Server(
                description = "Local Server",
                url = "http://localhost:8080"
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth"),
                @SecurityRequirement(name = "x-api-key")
        }

)
@SecuritySchemes(
        value = {
                @SecurityScheme(
                        name = "bearerAuth",
                        description = "JWT Token",
                        scheme = "bearer",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "JWT",
                        in = SecuritySchemeIn.HEADER
                ),
                @SecurityScheme(
                        name = "x-api-key",
                        description = "x-api-key",
                        scheme = "x-api-key",
                        type = SecuritySchemeType.APIKEY,
                        in = SecuritySchemeIn.HEADER
                )
        }
)
public class OpenApiConfig {
}
