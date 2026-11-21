package br.com.sanquim.api.dto.response;

public record AtribuicaoResponseDTO(
        Long id,
        Long professorId,
        String professorNome,
        Long turmaId,
        String turmaNome,
        Long disciplinaId,
        String disciplinaNome
) {}
