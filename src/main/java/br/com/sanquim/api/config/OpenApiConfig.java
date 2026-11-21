package br.com.sanquim.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST — Colégio Sanquim (SAC)")
                        .version("1.0.0")
                        .description("Contrato oficial de Backend do Sistema de Gestão Escolar do Colégio Sanquim (FATEC Arthur de Azevedo - Mogi Mirim | ADS 4º Semestre). Autentique em POST /api/auth/login e cole o token JWT no botão Authorize.")
                        .contact(new Contact()
                                .name("Equipe Backend ADS - FATEC Mogi Mirim")
                                .email("admin@sanquim.br")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira apenas o token JWT retornado em /api/auth/login")));
    }
}
