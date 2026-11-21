package br.com.sanquim.api.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record BoletimDisciplinaDTO(
        Long disciplinaId,
        String disciplinaNome,
        String professorNome,
        BigDecimal mediaParcial,
        String situacaoParcial,
        List<NotaBoletimItemDTO> avaliacoes
) {}
