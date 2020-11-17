package com.nse.excelupload.config;

import java.nio.ByteBuffer;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket apiDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
            .title("BOA Listing validation API")
            .version("0.0.1")
            .build();
        return new Docket(DocumentationType.SWAGGER_2)
            .host(null)
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(true)
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .ignoredParameterTypes(Pageable.class)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.nse.excelupload.controller"))
            .build();
    }
}
