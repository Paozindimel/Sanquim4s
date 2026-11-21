package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.PlanoAula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanoAulaRepository extends JpaRepository<PlanoAula, Long> {
    List<PlanoAula> findByTurmaIdInOrderByDataAulaDesc(List<Long> turmaIds);
    List<PlanoAula> findByProfessorIdOrderByDataAulaDesc(Long professorId);
}
