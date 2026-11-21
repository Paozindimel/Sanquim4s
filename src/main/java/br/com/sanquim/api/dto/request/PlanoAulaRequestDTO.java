package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PlanoAulaRequestDTO(
        @NotNull(message = "O ID da turma é obrigatório.")
        Long turmaId,

        @NotNull(message = "O ID da disciplina é obrigatório.")
        Long disciplinaId,

        @NotNull(message = "A data da aula é obrigatória.")
        LocalDate dataAula,

        @NotBlank(message = "O título da aula é obrigatório.")
        String titulo,

        @NotBlank(message = "O resumo/conteúdo lecionado é obrigatório.")
        String conteudo,

        String objetivos
) {}
