package br.com.southsystem.importer.config.storage;

import br.com.southsystem.importer.config.property.S3Property;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    
    private final S3Property s3Property;

    @Autowired
    public S3Config(S3Property s3Property) {
        this.s3Property = s3Property;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(s3Property.getAccessKey(), s3Property.getSecretKey());
        AmazonS3 amazonS3 = new AmazonS3Client(credentials, new ClientConfiguration());
        Region region = Region.getRegion(Regions.US_EAST_2);
        amazonS3.setRegion(region);
        return amazonS3;
    }
}
