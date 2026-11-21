package br.com.sanquim.api.dto.response;

public record DisciplinaResponseDTO(
        Long id,
        String nome,
        Integer cargaHoraria,
        boolean ativa,
        Long atribuicaoId,
        Long turmaId,
        Long professorId
) {}
