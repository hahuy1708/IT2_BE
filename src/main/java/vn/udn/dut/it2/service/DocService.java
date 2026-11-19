package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.udn.dut.it2.model.dto.DocDetailDto;
import vn.udn.dut.it2.model.entity.ProcedureDoc;
import vn.udn.dut.it2.repository.ProcedureDocRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocService {

    private final ProcedureDocRepository procedureDocRepository;

    public List<DocDetailDto.Response> getAllDocs() {
        return procedureDocRepository.findAllByOrderByIdAsc().stream()
                .map(DocService::toDto)
                .toList();
    }

    public Optional<DocDetailDto.Response> getDocById(Long id) {
        return procedureDocRepository.findById(id).map(DocService::toDto);
    }

    public Optional<DownloadPayload> buildFormDownload(Long id) {
        return procedureDocRepository.findById(id).map(doc -> {
            String fileName = (doc.getFileName() != null && !doc.getFileName().isBlank())
                    ? doc.getFileName()
                    : "bieu-mau-dut-" + doc.getId() + ".doc";
            byte[] content = renderPrintableFormHtml(doc).getBytes(StandardCharsets.UTF_8);
            return new DownloadPayload(fileName, content);
        });
    }

    private static DocDetailDto.Response toDto(ProcedureDoc entity) {
        String resolvedUrl = (entity.getFileUrl() != null && !entity.getFileUrl().isBlank())
                ? entity.getFileUrl()
                : "/api/docs/" + entity.getId() + "/download";
        return DocDetailDto.Response.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .summary(entity.getSummary())
                .steps(entity.getSteps() != null ? entity.getSteps() : List.of())
                .fileUrl(resolvedUrl)
                .tags(entity.getTags() != null ? entity.getTags() : List.of())
                .build();
    }

    private static String renderPrintableFormHtml(ProcedureDoc doc) {
        StringBuilder stepsHtml = new StringBuilder();
        if (doc.getSteps() != null) {
            for (DocDetailDto.Step step : doc.getSteps()) {
                stepsHtml.append("<li><strong>Bước ")
                        .append(step.getOrder())
                        .append(" - ")
                        .append(escapeHtml(step.getTitle()))
                        .append(":</strong> ")
                        .append(escapeHtml(step.getDescription()))
                        .append("</li>\n");
            }
        }
        return """
                <html xmlns:o="urn:schemas-microsoft-com:office:office"
                      xmlns:w="urn:schemas-microsoft-com:office:word"
                      xmlns="http://www.w3.org/TR/REC-html40">
                <head><meta charset="utf-8"><title>%s</title></head>
                <body style="font-family: 'Times New Roman', serif; font-size: 13pt; line-height: 1.5;">
                  <div style="text-align: center;">
                    <p style="margin:0;">ĐẠI HỌC ĐÀ NẴNG</p>
                    <p style="margin:0; font-weight:bold;">TRƯỜNG ĐẠI HỌC BÁCH KHOA</p>
                    <p style="margin:6px 0 18px 0;">--- *** ---</p>
                    <h2 style="text-transform: uppercase;">%s</h2>
                  </div>
                  <p><strong>Tóm tắt thủ tục:</strong> %s</p>
                  <p><strong>Thông tin sinh viên kê khai:</strong></p>
                  <ul>
                    <li>Họ và tên sinh viên: ........................................................................</li>
                    <li>Mã số sinh viên: ........................... Lớp: ...........................</li>
                    <li>Khoa: ........................................... Số điện thoại: ....................</li>
                    <li>Lý do / Nội dung đề nghị: ..............................................................</li>
                  </ul>
                  <p><strong>Hướng dẫn các bước thực hiện:</strong></p>
                  <ol>%s</ol>
                  <table style="width:100%%; margin-top:30px;">
                    <tr>
                      <td style="width:50%%; text-align:center;"><strong>Xác nhận của Khoa / Đơn vị</strong><br/><em>(Ký, ghi rõ họ tên)</em></td>
                      <td style="width:50%%; text-align:center;"><em>Đà Nẵng, ngày ..... tháng ..... năm 2026</em><br/><strong>Người làm đơn</strong><br/><em>(Ký, ghi rõ họ tên)</em></td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(
                escapeHtml(doc.getTitle()),
                escapeHtml(doc.getTitle()),
                escapeHtml(doc.getSummary()),
                stepsHtml.toString());
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public record DownloadPayload(String fileName, byte[] content) {}
}
