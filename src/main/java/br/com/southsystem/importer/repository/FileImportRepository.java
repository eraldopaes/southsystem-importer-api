package br.com.southsystem.importer.repository;

import br.com.southsystem.importer.domain.FileImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportRepository extends JpaRepository<FileImport, Integer> {
}
