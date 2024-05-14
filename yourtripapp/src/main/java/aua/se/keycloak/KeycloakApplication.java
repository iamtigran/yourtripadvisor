package aua.se.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
@SpringBootApplication
@Slf4j
public class KeycloakApplication {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(KeycloakApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.setWebApplicationType(WebApplicationType.SERVLET);
        Environment env = application.run(args).getEnvironment();
        logApplicationStartup(env);
    }



private static void logApplicationStartup(Environment env) {
    String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
    String applicationName = env.getProperty("spring.application.name");
    String serverPort = env.getProperty("server.port");
    String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
    String hostAddress = "localhost";
    try {
        hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
        log.warn("The host name could not be determined, using `localhost` as fallback");
    }
    log.info(
            """

        ----------------------------------------------------------
        \tApplication '{}' is running! Access URLs:
        \tLocal: \t\t{}://localhost:{}{}
        \tExternal: \t{}://{}:{}{}
        \tProfile(s): \t{}
        ----------------------------------------------------------""",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
    );
}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods(CorsConfiguration.ALL)
                        .allowedHeaders(CorsConfiguration.ALL)
                        .allowedOriginPatterns(CorsConfiguration.ALL);
            }
        };
    }



    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("YourTripAdvisor Ready Event");
    }

}