package vn.udn.dut.it2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.udn.dut.it2.model.entity.ProcedureDoc;

import java.util.List;

@Repository
public interface ProcedureDocRepository extends JpaRepository<ProcedureDoc, Long> {
    List<ProcedureDoc> findAllByOrderByIdAsc();
}
