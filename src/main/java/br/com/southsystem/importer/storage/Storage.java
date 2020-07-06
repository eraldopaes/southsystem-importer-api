package br.com.southsystem.importer.storage;

import br.com.southsystem.importer.domain.enums.BucketTypeEnum;
import org.springframework.web.multipart.MultipartFile;

public interface Storage {

    String save(MultipartFile files, BucketTypeEnum bucketTypeEnum);

    String getDownloadUrl(String filename, BucketTypeEnum bucketTypeEnum);
}
