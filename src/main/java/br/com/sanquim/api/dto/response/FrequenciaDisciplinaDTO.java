package br.com.sanquim.api.dto.response;

import java.math.BigDecimal;

public record FrequenciaDisciplinaDTO(
        Long disciplinaId,
        String disciplinaNome,
        int totalAulas,
        int totalPresencas,
        int totalFaltas,
        BigDecimal percentualPresenca,
        String situacaoFrequencia
) {}
