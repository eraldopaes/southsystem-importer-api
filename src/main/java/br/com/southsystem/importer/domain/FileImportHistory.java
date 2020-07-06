package br.com.southsystem.importer.domain;

import br.com.southsystem.importer.domain.enums.FileImportStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@DynamicUpdate
@Table(name = "file_import_history")
public class FileImportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_import_history_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_import_history_status")
    private FileImportStatusEnum status;

    @OneToOne
    @JoinColumn(name = "file_import_history_file_import_id", referencedColumnName = "file_import_id")
    private FileImport fileImport;

    @Column(name = "file_import_history_date")
    private LocalDateTime date;
}
