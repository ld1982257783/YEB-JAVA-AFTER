package com.xxxx.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2        //开启swagger
public class Swagger2Config {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                //设置基本信息
                .apiInfo(apiInfo())
                //开启扫描
                .select()
                //设置扫描包
                .apis(RequestHandlerSelectors.basePackage("com.xxxx.server.controller"))
                //设置过滤路径
                .paths(PathSelectors.any())
                //创建
                .build()
                //swagger 添加全局 Authorize
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }


    @Bean
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("云E办接口文档")
                .description("云E办接口文档")
                .contact(new Contact("xxxx","http://localhost:8081/doc.html","xxxx@xxxx.com"))
                .version("1.0")
                .build();
    }


    private List<ApiKey> securitySchemes(){
        //设置请求头信息
        ArrayList<ApiKey> result = new ArrayList<>();

        ApiKey apiKey = new ApiKey("Authorization","Authorization", "Header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts(){
        //设置需要认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegx) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegx))
                .build();
    }


    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        //授权范围
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization",authorizationScopes));
        return result;

    }


}
