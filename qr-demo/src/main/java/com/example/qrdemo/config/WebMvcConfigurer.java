package com.example.qrdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author qzz
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {


    /**
     * 添加静态资源映射路径，css、js等都放在classpath下的static中
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * addResourceHandler 指的是对外暴露的访问路径
         * addResourceLocations 指的是文件配置的目录
         */

        //文件上传路径映射
        registry.addResourceHandler("/mimi/upload/**")
                .addResourceLocations("file:D:/mimi/upload/");

        super.addResourceHandlers(registry);
    }
}

