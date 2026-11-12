package tech.miqueias.bento.io.shared;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.Contact;

@OpenAPIDefinition(
    info = @Info(
        title = "Sistema de Comandas API",
        version = "1.0.0",
        description = "API REST para gerenciamento de comandas de bar/restaurante",
        contact = @Contact(name = "YourMenu", email = "suporte@yourmenu.com")
    )
)
@SecurityScheme(
    securitySchemeName = "jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Insira o token JWT gerado no login."
)
public class OpenApiConfig extends Application {
}
