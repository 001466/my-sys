package org.easy.gateway.swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;


@Configuration //必须存在
@EnableSwagger2WebFlux
public class SwaggerConfig{
    private static final String splitor = ";";
    @Bean
    public Docket customDocket() {
    	return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("接口文档")
                        .title("接口文档")
                        .version("1.0.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.easy.gateway.controller"))
                .paths(PathSelectors.any())
                .build();
    }
 
}