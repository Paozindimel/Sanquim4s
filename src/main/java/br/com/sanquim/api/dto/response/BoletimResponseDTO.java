package br.com.sanquim.api.dto.response;

import java.util.List;

public record BoletimResponseDTO(
        Long alunoId,
        String alunoNome,
        String ra,
        List<String> turmasMatriculadas,
        List<BoletimDisciplinaDTO> disciplinas
) {}
