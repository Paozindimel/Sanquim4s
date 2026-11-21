package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByTurmaIdOrderByDataEntregaAsc(Long turmaId);
    List<Avaliacao> findByProfessorIdOrderByDataEntregaDesc(Long professorId);
}
