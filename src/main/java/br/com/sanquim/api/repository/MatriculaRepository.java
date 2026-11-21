package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Matricula;
import br.com.sanquim.api.domain.enums.SituacaoMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);
    boolean existsByAlunoIdAndTurmaIdAndSituacao(Long alunoId, Long turmaId, SituacaoMatricula situacao);
    List<Matricula> findByTurmaIdAndSituacao(Long turmaId, SituacaoMatricula situacao);
    List<Matricula> findByAlunoIdAndSituacao(Long alunoId, SituacaoMatricula situacao);
}
