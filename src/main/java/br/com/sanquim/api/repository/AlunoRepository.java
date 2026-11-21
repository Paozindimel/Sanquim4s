package br.com.sanquim.api.repository;

import br.com.sanquim.api.domain.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByUsuarioId(Long usuarioId);
    Optional<Aluno> findByEmailIgnoreCase(String email);
    boolean existsByRa(String ra);
    boolean existsByCpf(String cpf);
    boolean existsByEmailIgnoreCase(String email);
}
