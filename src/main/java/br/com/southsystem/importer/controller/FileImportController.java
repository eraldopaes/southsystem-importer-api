package br.com.southsystem.importer.controller;

import br.com.southsystem.importer.domain.FileImport;
import br.com.southsystem.importer.dto.FileImportDownloadUrlDTO;
import br.com.southsystem.importer.service.FileImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/southsystem/api/v1")
public class FileImportController {

    private final FileImportService fileImportService;

    @Autowired
    public FileImportController(FileImportService fileImportService) {
        this.fileImportService = fileImportService;
    }

    @GetMapping(value = "/file-imports/{id}")
    public ResponseEntity<FileImport> status(@PathVariable Integer id) {
        FileImport fileImport = fileImportService.findById(id);
        return ResponseEntity.ok(fileImport);
    }

    @GetMapping(value = "/file-imports/{id}/resume")
    public ResponseEntity<FileImportDownloadUrlDTO> resume(@PathVariable Integer id) {
        FileImportDownloadUrlDTO fileImportDownloadUrlDTO = fileImportService.downloadResume(id);
        return ResponseEntity.ok(fileImportDownloadUrlDTO);
    }

    @PostMapping(value = "/file-imports")
    public ResponseEntity<FileImport> upload(@RequestParam("attachment") MultipartFile attachment) {
        FileImport uploaded = fileImportService.upload(attachment);
        return ResponseEntity.ok(uploaded);
    }
}
