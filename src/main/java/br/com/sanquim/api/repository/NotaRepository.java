package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    Optional<Nota> findByAvaliacaoIdAndAlunoId(Long avaliacaoId, Long alunoId);
    List<Nota> findByAlunoId(Long alunoId);
    List<Nota> findByAvaliacaoId(Long avaliacaoId);
}
