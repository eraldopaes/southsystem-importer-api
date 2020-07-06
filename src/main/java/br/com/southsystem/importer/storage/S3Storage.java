package br.com.southsystem.importer.storage;

import br.com.southsystem.importer.config.property.CloudFrontProperty;
import br.com.southsystem.importer.config.property.S3Property;
import br.com.southsystem.importer.domain.enums.BucketTypeEnum;
import br.com.southsystem.importer.exceptionhandler.BusinessException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class S3Storage implements Storage {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3Storage.class);

    private final AmazonS3 amazonS3;
    private final S3Property s3Property;
    private final CloudFrontProperty cloudFrontProperty;

    @Autowired
    public S3Storage(AmazonS3 amazonS3, S3Property s3Property, CloudFrontProperty cloudFrontProperty) {
        this.amazonS3 = amazonS3;
        this.s3Property = s3Property;
        this.cloudFrontProperty = cloudFrontProperty;
    }

    @Override
    public String save(MultipartFile files, BucketTypeEnum bucketTypeEnum) {
        String newName = null;
        if (files != null) {
            newName = renameFile(files.getOriginalFilename());
            try {

                AccessControlList acl = new AccessControlList();
                sendFile(newName, files, acl, bucketTypeEnum);

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro salvando arquivo no S3", e);
            }
        }
        return newName;
    }

    @Override
    public String getDownloadUrl(String filename, BucketTypeEnum bucketTypeEnum) {

        switch (bucketTypeEnum) {

            case FILE_OUTPUT:
                if (!StringUtils.isEmpty(filename)) {
                    String url = cloudFrontProperty.getBaseUrl() + "/" + BucketTypeEnum.FILE_OUTPUT.getName() + "/" + renameToProcessedFilename(filename);
                    LOGGER.info("Path para imagem: " + url);
                    return url;
                }
        }

        throw new BusinessException("storage.file-not-found");
    }

    private ObjectMetadata sendFile(String newName, MultipartFile file, AccessControlList acl,
                                    BucketTypeEnum bucketTypeEnum) throws IOException {

        String path;

        switch (bucketTypeEnum) {
            case FILE_INPUT:
                path = "file-input/" + newName;
                LOGGER.info("Path para imagem: " + path);
                break;
            case FILE_OUTPUT:
                path = "file-output/" + newName;
                LOGGER.info("Path para imagem: " + path);
                break;
            default:
                throw new BusinessException("storage-2");
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        PutObjectRequest putObjectRequest = new PutObjectRequest(s3Property.getBucketName(), path, file.getInputStream(), metadata);
        putObjectRequest.withAccessControlList(acl);
        amazonS3.putObject(putObjectRequest);

        return metadata;
    }

    private String renameFile(String originalName) {
        String newName = UUID.randomUUID().toString() + "_" + originalName;
        LOGGER.debug(String.format("Nome original: %s\nNovo nome do arquivo: %s", originalName, newName));
        return newName;
    }

    private String renameToProcessedFilename(String filename) {
        String filenameWithoutExtension = filename.replace(".dat", "");
        return filenameWithoutExtension + ".done.dat";
    }
}
