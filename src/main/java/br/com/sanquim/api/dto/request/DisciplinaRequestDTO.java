package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DisciplinaRequestDTO(
        @NotBlank(message = "O nome da disciplina é obrigatório.")
        String nome,

        @NotNull(message = "A carga horária é obrigatória.")
        @Positive(message = "A carga horária deve ser maior que zero.")
        Integer cargaHoraria,

        Long turmaId,
        Long professorId
) {}
