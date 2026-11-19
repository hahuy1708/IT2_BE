package vn.udn.dut.it2.model.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.udn.dut.it2.model.converter.DocStepListJsonConverter;
import vn.udn.dut.it2.model.converter.StringListJsonConverter;
import vn.udn.dut.it2.model.dto.DocDetailDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "procedure_docs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Builder.Default
    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    @Convert(converter = DocStepListJsonConverter.class)
    @Column(name = "steps_json", columnDefinition = "TEXT")
    private List<DocDetailDto.Step> steps = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
