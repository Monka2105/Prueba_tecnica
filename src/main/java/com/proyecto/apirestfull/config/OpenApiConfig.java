package com.proyecto.apirestfull.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST Full - Prueba Técnica Financiera")
                        .description("API para la gestión de clientes, cuentas y transacciones")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Proyecto Spring")
                                .email("emanuel.morenom1403@gmail.com")));
    }
}
