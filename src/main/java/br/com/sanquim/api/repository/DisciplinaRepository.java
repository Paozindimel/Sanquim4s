package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
