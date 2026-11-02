package com.kaio.limbus_admin_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI limbusAdminOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Limbus Admin API")
                        .version("1.0.0")
                        .description("""
                                API de administração do catálogo do gacha de Limbus Company.
                                Cadastra Identities, E.G.O. e Banners consumidos pela Limbus Gacha API.

                                Raridades: ZERO = 0, DOUBLE = 00, TRIPLE = 000.
                                Todo erro responde no formato { "error": "mensagem" }.
                                """));
    }
}
