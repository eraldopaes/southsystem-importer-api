package br.com.southsystem.importer.config.error;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ApiErrorMessageDefaultConfig {

    @Bean
    public MessageSource apiErrorMessageSource() {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/api_errors");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
