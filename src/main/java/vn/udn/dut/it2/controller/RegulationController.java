package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.udn.dut.it2.model.entity.Regulation;
import vn.udn.dut.it2.service.RegulationService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/regulations")
@RequiredArgsConstructor
@Tag(name = "2. Regulations & Legal Docs", description = "API Tra cứu và Danh mục Quy chế Pháp quy")
public class RegulationController {

    private final RegulationService regulationService;

    @GetMapping
    @Operation(summary = "Tìm kiếm và lọc danh mục văn bản quy chế")
    public ResponseEntity<List<Regulation>> searchRegulations(
            @RequestParam(required = false, defaultValue = "ALL") String category,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(regulationService.search(category, keyword));
    }

    @GetMapping("/by-number")
    @Operation(summary = "Tìm văn bản quy chế theo số hiệu (VD: 1414/QĐ-ĐHBK)")
    public ResponseEntity<Regulation> getRegulationByNumber(@RequestParam String documentNumber) {
        return regulationService.getByDocumentNumber(documentNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết một văn bản quy chuẩn (phục vụ Document Viewer)")
    public ResponseEntity<Regulation> getRegulationById(@PathVariable Long id) {
        return regulationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/content")
    @Operation(summary = "Lấy nội dung số hóa (HTML/Markdown) của văn bản quy chế")
    public ResponseEntity<RegulationService.RegulationContent> getRegulationContent(@PathVariable Long id) {
        return regulationService.getContent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/pdf")
    @Operation(summary = "Tải/xem trực tiếp file PDF gốc của văn bản quy chế (phục vụ react-pdf)")
    public ResponseEntity<Resource> streamRegulationPdf(@PathVariable Long id) {
        return regulationService.resolvePdfResource(id)
                .map(resource -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDisposition(ContentDisposition.inline()
                            .filename(resource.getFilename() != null ? resource.getFilename() : "regulation-" + id + ".pdf",
                                    StandardCharsets.UTF_8)
                            .build());
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
