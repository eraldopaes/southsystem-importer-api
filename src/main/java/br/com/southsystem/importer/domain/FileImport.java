package br.com.southsystem.importer.domain;

import br.com.southsystem.importer.domain.enums.FileImportStatusEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file_import")
public class FileImport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_import_id")
    private Integer id;

    @Column(name = "file_import_filename")
    private String filename;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_import_status")
    private FileImportStatusEnum status;

    @Column(name = "file_import_error")
    private String error;

    @Column(name = "file_import_error_line")
    private String errorLine;
}
