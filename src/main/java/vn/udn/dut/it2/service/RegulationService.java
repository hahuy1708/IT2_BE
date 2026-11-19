package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import vn.udn.dut.it2.config.StorageProperties;
import vn.udn.dut.it2.model.entity.Regulation;
import vn.udn.dut.it2.repository.RegulationRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegulationService {

    private final RegulationRepository regulationRepository;
    private final StorageProperties storageProperties;

    public List<Regulation> search(String category, String keyword) {
        return regulationRepository.searchRegulations(category, keyword);
    }

    public Optional<Regulation> getById(Long id) {
        return regulationRepository.findById(id);
    }

    public Optional<Regulation> getByDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            return Optional.empty();
        }
        return regulationRepository.findFirstByDocumentNumberIgnoreCase(documentNumber.trim());
    }

    public List<Regulation> getAll() {
        return regulationRepository.findAll();
    }

    public Optional<Resource> resolvePdfResource(Long id) {
        return regulationRepository.findById(id)
                .flatMap(reg -> findFileByPrefix(storageProperties.getPdfDir(), reg.getDocumentId(), ".pdf"))
                .map(FileSystemResource::new);
    }

    public Optional<RegulationContent> getContent(Long id) {
        return regulationRepository.findById(id).map(reg -> {
            String markdown = findFileByPrefix(storageProperties.getMarkdownDir(), reg.getDocumentId(), ".md")
                    .map(path -> {
                        try {
                            return Files.readString(path, StandardCharsets.UTF_8);
                        } catch (IOException ex) {
                            log.warn("Could not read markdown file {}: {}", path, ex.getMessage());
                            return null;
                        }
                    })
                    .orElse(null);
            return new RegulationContent(
                    reg.getId(),
                    reg.getDocumentNumber(),
                    reg.getTitle(),
                    reg.getContentHtml(),
                    markdown);
        });
    }

    private Optional<Path> findFileByPrefix(String dirRaw, String documentId, String extension) {
        if (dirRaw == null || dirRaw.isBlank() || documentId == null || documentId.isBlank()) {
            return Optional.empty();
        }
        Path dir = Path.of(dirRaw).toAbsolutePath().normalize();
        if (!Files.isDirectory(dir)) {
            return Optional.empty();
        }
        String prefix = documentId.trim() + "_";
        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString();
                        return name.startsWith(prefix) && name.toLowerCase().endsWith(extension);
                    })
                    .findFirst();
        } catch (IOException ex) {
            log.warn("Failed to list directory {}: {}", dir, ex.getMessage());
            return Optional.empty();
        }
    }

    public record RegulationContent(
            Long id,
            String documentNumber,
            String title,
            String contentHtml,
            String markdown) {}
}
