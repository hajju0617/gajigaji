package com.green.project2nd.common.model;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Configurable;

@OpenAPIDefinition(
        info = @Info(
                title = "2차"
                , description = "Greengram with React"
                , version = "v3"


        ),
        security = @SecurityRequirement(name = "authorization")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP
        , name = "authorization"
        , in = SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "Bearer"
)
public class SwaggerConfiguration {
}
