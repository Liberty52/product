package com.liberty52.product.global.config;

import com.liberty.authentication.web.aspect.LBPreAuthorizeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
    @Bean
    public LBPreAuthorizeAspect lbPreAuthorizeAspect() {
        return new LBPreAuthorizeAspect();
    }
}
