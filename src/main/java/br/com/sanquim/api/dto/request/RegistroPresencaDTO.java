package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegistroPresencaDTO(
        @NotNull(message = "O ID do aluno é obrigatório.")
        Long alunoId,

        @NotNull(message = "O indicador de presença é obrigatório.")
        Boolean presente
) {}
