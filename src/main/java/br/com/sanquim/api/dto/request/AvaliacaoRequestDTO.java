package br.com.sanquim.api.dto.request;

import br.com.sanquim.api.domain.enums.TipoAvaliacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AvaliacaoRequestDTO(
        @NotBlank(message = "O título da avaliação é obrigatório.")
        String titulo,

        @NotNull(message = "O tipo da avaliação é obrigatório (PROVA, SEMINARIO, TRABALHO, ATIVIDADE, SIMULADO).")
        TipoAvaliacao tipo,

        @NotNull(message = "O peso da avaliação é obrigatório.")
        @DecimalMin(value = "0.1", message = "O peso deve ser no mínimo 0.1.")
        BigDecimal peso,

        @NotNull(message = "A nota máxima da avaliação é obrigatória.")
        @DecimalMin(value = "1.0", message = "A nota máxima deve ser no mínimo 1.0.")
        BigDecimal notaMaxima,

        @NotNull(message = "A data de entrega/aplicação é obrigatória.")
        LocalDate dataEntrega,

        @NotNull(message = "O ID da turma é obrigatório.")
        Long turmaId,

        @NotNull(message = "O ID da disciplina é obrigatório.")
        Long disciplinaId
) {}
