package vn.udn.dut.it2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.udn.dut.it2.model.dto.DocDetailDto;
import vn.udn.dut.it2.model.entity.ProcedureDoc;
import vn.udn.dut.it2.repository.ProcedureDocRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocServiceTest {

    @Mock
    private ProcedureDocRepository repository;

    @Test
    void getDocById_returnsEntityStepsAndDownloadUrl() {
        ProcedureDoc entity = ProcedureDoc.builder()
                .id(1L)
                .slug("hoc-bong-kkht")
                .title("Hướng dẫn đăng ký xét Học bổng KKHT")
                .summary("Tóm tắt")
                .fileName("mau-don-hb.doc")
                .fileUrl("/api/docs/1/download")
                .tags(List.of("Học bổng", "Biểu mẫu"))
                .steps(List.of(new DocDetailDto.Step(1, "Kiểm tra điều kiện", "Đối chiếu GPA và ĐRL")))
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        DocService service = new DocService(repository);
        Optional<DocDetailDto.Response> result = service.getDocById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Hướng dẫn đăng ký xét Học bổng KKHT");
        assertThat(result.get().getSteps()).hasSize(1);
        assertThat(result.get().getFileUrl()).isEqualTo("/api/docs/1/download");

        Optional<DocService.DownloadPayload> download = service.buildFormDownload(1L);
        assertThat(download).isPresent();
        assertThat(download.get().fileName()).isEqualTo("mau-don-hb.doc");
        String html = new String(download.get().content(), StandardCharsets.UTF_8);
        assertThat(html).contains("Hướng dẫn đăng ký xét Học bổng KKHT");
        assertThat(html).contains("Kiểm tra điều kiện");
    }
}
