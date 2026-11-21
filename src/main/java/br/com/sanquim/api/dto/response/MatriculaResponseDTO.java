package br.com.sanquim.api.dto.response;

import java.time.LocalDate;

public record MatriculaResponseDTO(
        Long id,
        Long alunoId,
        String alunoNome,
        String alunoRa,
        Long turmaId,
        String turmaNome,
        LocalDate dataMatricula,
        String situacao
) {}
