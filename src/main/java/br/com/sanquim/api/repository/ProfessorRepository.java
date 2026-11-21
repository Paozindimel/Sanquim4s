package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsuarioId(Long usuarioId);
    Optional<Professor> findByEmailIgnoreCase(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmailIgnoreCase(String email);
}
