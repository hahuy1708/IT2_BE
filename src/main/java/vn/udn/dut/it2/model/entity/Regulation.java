package vn.udn.dut.it2.model.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.udn.dut.it2.model.converter.StringListJsonConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regulations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false)
    private String category; // HOC_BONG, HOC_VU, THU_TUC_MOT_CUA

    @Column(name = "field")
    private String field;

    @Column(nullable = false)
    private String issuer;

    @Column(name = "status")
    private String status;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "content_html", columnDefinition = "TEXT")
    private String contentHtml;

    @Builder.Default
    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "tags", columnDefinition = "TEXT")
    private List<String> tags = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
