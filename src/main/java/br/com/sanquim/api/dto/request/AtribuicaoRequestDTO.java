package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record AtribuicaoRequestDTO(
        @NotNull(message = "O ID do professor é obrigatório.")
        Long professorId,

        @NotNull(message = "O ID da turma é obrigatório.")
        Long turmaId,

        @NotNull(message = "O ID da disciplina é obrigatório.")
        Long disciplinaId
) {}
