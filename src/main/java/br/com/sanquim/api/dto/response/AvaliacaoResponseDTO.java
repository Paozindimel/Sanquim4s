package br.com.sanquim.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AvaliacaoResponseDTO(
        Long id,
        String titulo,
        String tipo,
        BigDecimal peso,
        BigDecimal notaMaxima,
        LocalDate dataEntrega,
        Long turmaId,
        String turmaNome,
        Long disciplinaId,
        String disciplinaNome,
        String professorNome
) {}
