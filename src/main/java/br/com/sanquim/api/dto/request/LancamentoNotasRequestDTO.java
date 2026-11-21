package br.com.sanquim.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LancamentoNotasRequestDTO(
        @NotEmpty(message = "Informe ao menos uma nota para lançamento.")
        @Valid
        List<NotaItemRequestDTO> notas
) {}
