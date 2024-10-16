package com.enigma.tokonyadia_api.bean;

import com.enigma.tokonyadia_api.security.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {
    @Bean
    public AuditorAware<String> auditorProvider(){
        return new SecurityAuditorAware();
    }
}
