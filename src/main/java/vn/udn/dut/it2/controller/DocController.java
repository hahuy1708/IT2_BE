package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.udn.dut.it2.model.dto.DocDetailDto;
import vn.udn.dut.it2.service.DocService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/docs")
@RequiredArgsConstructor
@Tag(name = "6. Docs Detail", description = "API Xem chi tiết thủ tục/quy định (step-by-step & tải biểu mẫu từ PostgreSQL)")
public class DocController {

    private final DocService docService;

    @GetMapping
    @Operation(summary = "Danh sách các thủ tục hành chính & hướng dẫn có sẵn")
    public ResponseEntity<List<DocDetailDto.Response>> listDocs() {
        return ResponseEntity.ok(docService.getAllDocs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết bài viết/thủ tục gồm các bước thực hiện và link tải biểu mẫu")
    public ResponseEntity<DocDetailDto.Response> getDocById(@PathVariable Long id) {
        return docService.getDocById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Tải biểu mẫu Word (.doc) kèm hướng dẫn các bước thực hiện của thủ tục")
    public ResponseEntity<byte[]> downloadDocForm(@PathVariable Long id) {
        return docService.buildFormDownload(id)
                .map(payload -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType("application/msword; charset=UTF-8"));
                    headers.setContentDisposition(ContentDisposition.attachment()
                            .filename(payload.fileName(), StandardCharsets.UTF_8)
                            .build());
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(payload.content());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
