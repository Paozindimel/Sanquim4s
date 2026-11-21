package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {
    boolean existsByAlunoIdAndTurmaIdAndDisciplinaIdAndDataAula(
            Long alunoId, Long turmaId, Long disciplinaId, LocalDate dataAula);

    boolean existsByTurmaIdAndDisciplinaIdAndDataAula(
            Long turmaId, Long disciplinaId, LocalDate dataAula);

    List<Frequencia> findByAlunoIdOrderByDataAulaDesc(Long alunoId);
}
