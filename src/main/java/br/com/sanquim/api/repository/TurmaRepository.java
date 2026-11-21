package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
