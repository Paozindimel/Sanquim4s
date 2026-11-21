package br.com.sanquim.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record NotaBoletimItemDTO(
        Long avaliacaoId,
        String tituloAvaliacao,
        String tipo,
        LocalDate dataEntrega,
        BigDecimal peso,
        BigDecimal notaMaxima,
        BigDecimal notaObtida,
        String observacao
) {}
