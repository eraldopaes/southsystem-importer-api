package br.com.southsystem.importer.repository;

import br.com.southsystem.importer.domain.FileImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportHistoryRepository extends JpaRepository<FileImportHistory, Integer> {
}
