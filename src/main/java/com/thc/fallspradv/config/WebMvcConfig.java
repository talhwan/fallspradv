package com.thc.fallspradv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //파일 접근하는 통로 열어두고 싶다면 사용하면 됨
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        // 절대경로 file:///c:/upload/
        // 상대경로 file:./upload/
        registry
                .addResourceHandler("/files/**")
                //접근할 파일이 있는 위치를 지정
                .addResourceLocations("file:" + "C:/workspace/uploadfiles/")
                .setCachePeriod(60 * 60) // 초 단위
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}