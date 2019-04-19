package com.myFirstProject.myFirstProject.config;

import com.myFirstProject.myFirstProject.security.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//просто реєструю AuthInterceptor( щоб він почав працювати) для всіх полів тому що  - "/**/"
@Configuration
public class AuthMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**/");
    }

}
