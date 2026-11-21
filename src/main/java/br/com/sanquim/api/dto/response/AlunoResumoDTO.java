package br.com.sanquim.api.dto.response;

public record AlunoResumoDTO(
        Long alunoId,
        String nome,
        String ra
) {}
