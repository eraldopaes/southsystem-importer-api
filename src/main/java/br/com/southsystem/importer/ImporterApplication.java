package br.com.southsystem.importer;

import br.com.southsystem.importer.config.property.CloudFrontProperty;
import br.com.southsystem.importer.config.property.S3Property;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({S3Property.class, CloudFrontProperty.class})
public class ImporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }
}
