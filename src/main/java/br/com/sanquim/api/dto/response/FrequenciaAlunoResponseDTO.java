package br.com.sanquim.api.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record FrequenciaAlunoResponseDTO(
        Long alunoId,
        String alunoNome,
        String ra,
        int totalAulasGlobal,
        int totalPresencasGlobal,
        int totalFaltasGlobal,
        BigDecimal percentualGlobalFrequencia,
        String situacaoGlobal,
        List<FrequenciaDisciplinaDTO> porDisciplina
) {}
