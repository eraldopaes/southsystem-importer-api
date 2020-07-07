package br.com.southsystem.importer.service;

import br.com.southsystem.importer.domain.FileImport;
import br.com.southsystem.importer.domain.FileImportHistory;
import br.com.southsystem.importer.domain.enums.BucketTypeEnum;
import br.com.southsystem.importer.domain.enums.FileImportStatusEnum;
import br.com.southsystem.importer.dto.FileImportDTO;
import br.com.southsystem.importer.dto.FileImportDownloadUrlDTO;
import br.com.southsystem.importer.exceptionhandler.BusinessException;
import br.com.southsystem.importer.repository.FileImportRepository;
import br.com.southsystem.importer.storage.Storage;
import br.com.southsystem.importer.utils.LocalDateTimeUtils;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static br.com.southsystem.importer.config.broker.RabbitMQConfig.*;
import static java.util.Objects.isNull;

@Service
public class FileImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileImportService.class);

    private final Storage storage;
    private final RabbitTemplate rabbitTemplate;
    private final LocalDateTimeUtils localDateTimeUtils;
    private final FileImportRepository fileImportRepository;
    private final FileImportHistoryService fileImportHistoryService;

    @Autowired
    public FileImportService(Storage storage,
                             RabbitTemplate rabbitTemplate,
                             FileImportRepository fileImportRepository,
                             LocalDateTimeUtils localDateTimeUtils,
                             FileImportHistoryService fileImportHistoryService) {
        this.storage = storage;
        this.rabbitTemplate = rabbitTemplate;
        this.fileImportRepository = fileImportRepository;
        this.localDateTimeUtils = localDateTimeUtils;
        this.fileImportHistoryService = fileImportHistoryService;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileImport findById(Integer id) {
        return fileImportRepository.findById(id)
                .orElseThrow(() -> new BusinessException("file-import-service.not-found"));
    }

    public FileImportDownloadUrlDTO downloadResume(Integer id) {
        FileImport fileImport = findById(id);
        String downloadUrl = storage.getDownloadUrl(fileImport.getFilename(), BucketTypeEnum.FILE_OUTPUT);
        FileImportDownloadUrlDTO fileImportDownloadUrlDTO = new FileImportDownloadUrlDTO();
        fileImportDownloadUrlDTO.setUrl(downloadUrl);
        return fileImportDownloadUrlDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public FileImport upload(MultipartFile multipartFile) {
        validateFile(multipartFile);
        return save(multipartFile);
    }

    private void validateFile(MultipartFile multipartFile) {
        if (isNull(multipartFile)) {
            LOGGER.error("Arquivo inválido");
            throw new BusinessException("file-import-service.invalid-file");
        }

        if (!getExtension(multipartFile.getOriginalFilename()).equals("dat")) {
            LOGGER.error("Arquivo inválido");
            throw new BusinessException("file-import-service.invalid-file");
        }
    }

    private String getExtension(String filename) {
        return Files.getFileExtension(filename);
    }

    private FileImport save(MultipartFile multipartFile) {

        String fileSaved = storage.save(multipartFile, BucketTypeEnum.FILE_INPUT);

        FileImport fileImport = new FileImport();
        fileImport.setFilename(fileSaved);
        fileImport.setStatus(FileImportStatusEnum.WAITING_FOR_PROCESS);
        FileImport fileImportSaved = fileImportRepository.save(fileImport);

        FileImportHistory fileImportHistory = new FileImportHistory();
        fileImportHistory.setFileImport(fileImportSaved);
        fileImportHistory.setStatus(FileImportStatusEnum.WAITING_FOR_PROCESS);
        fileImportHistory.setDate(localDateTimeUtils.getLocalDateTime());
        fileImportHistoryService.save(fileImportHistory);

        return fileImportSaved;
    }

    public void sendToProcess(FileImport fileImport) {
        FileImportDTO fileImportDTO = new FileImportDTO();
        fileImportDTO.setId(fileImport.getId());
        rabbitTemplate.convertAndSend(FILE_IMPORT_EXCHANGE, FILE_IMPORT_BINDING, fileImportDTO);
    }
}
