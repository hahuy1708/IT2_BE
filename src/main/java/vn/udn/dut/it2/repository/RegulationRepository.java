package vn.udn.dut.it2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.udn.dut.it2.model.entity.Regulation;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Long> {

    List<Regulation> findByCategory(String category);

    Optional<Regulation> findFirstByDocumentNumberIgnoreCase(String documentNumber);

    Optional<Regulation> findFirstByDocumentId(String documentId);

    @Query("SELECT r FROM Regulation r WHERE " +
           "(:category IS NULL OR :category = 'ALL' OR r.category = :category) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.documentNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(COALESCE(r.summary, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(COALESCE(r.field, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY r.issuedDate DESC NULLS LAST, r.id ASC")
    List<Regulation> searchRegulations(@Param("category") String category, @Param("keyword") String keyword);
}
