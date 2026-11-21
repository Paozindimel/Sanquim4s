package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record NotaItemRequestDTO(
        @NotNull(message = "O ID do aluno é obrigatório.")
        Long alunoId,

        @NotNull(message = "O valor da nota é obrigatório.")
        @DecimalMin(value = "0.0", message = "A nota não pode ser negativa.")
        BigDecimal valor,

        String observacao
) {}
