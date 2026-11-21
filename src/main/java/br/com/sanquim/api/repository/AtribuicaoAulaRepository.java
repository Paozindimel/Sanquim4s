package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.AtribuicaoAula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AtribuicaoAulaRepository extends JpaRepository<AtribuicaoAula, Long> {
    List<AtribuicaoAula> findByProfessorId(Long professorId);
    boolean existsByTurmaIdAndDisciplinaId(Long turmaId, Long disciplinaId);
    boolean existsByProfessorIdAndTurmaIdAndDisciplinaId(Long professorId, Long turmaId, Long disciplinaId);
    Optional<AtribuicaoAula> findByTurmaIdAndDisciplinaId(Long turmaId, Long disciplinaId);
}
