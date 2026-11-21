package br.com.sanquim.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NotaResponseDTO(
        Long id,
        Long avaliacaoId,
        String avaliacaoTitulo,
        Long alunoId,
        String alunoNome,
        String alunoRa,
        BigDecimal valor,
        BigDecimal notaMaxima,
        String observacao,
        LocalDateTime atualizadoEm
) {}
