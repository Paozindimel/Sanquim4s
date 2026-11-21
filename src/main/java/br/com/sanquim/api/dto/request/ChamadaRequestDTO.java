package br.com.sanquim.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record ChamadaRequestDTO(
        @NotNull(message = "O ID da turma é obrigatório.")
        Long turmaId,

        @NotNull(message = "O ID da disciplina é obrigatório.")
        Long disciplinaId,

        @NotNull(message = "A data da aula é obrigatória (AAAA-MM-DD).")
        LocalDate dataAula,

        @NotEmpty(message = "A lista de registros de presença não pode ser vazia.")
        @Valid
        List<RegistroPresencaDTO> registros
) {}
