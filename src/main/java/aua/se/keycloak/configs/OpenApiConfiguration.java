package aua.se.keycloak.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@SecurityScheme(
        name = "Keycloak",
        scheme = "bearer",
        type = SecuritySchemeType.OPENIDCONNECT,
        openIdConnectUrl = "http://localhost:8890/realms/dev/.well-known/openid-configuration",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {


    @Bean
    @Lazy
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("YourTripAdvisor API v1")
                .packagesToScan("aua.se")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("YourTripAdvisor API")
                        .description("YourTripAdvisor REST API")
                        .version("v1")

                ))
                .build();
    }
}
