package br.com.sanquim.api.dto.response;

public record TurmaResponseDTO(
        Long id,
        String nome,
        String curso,
        String local,
        String periodo,
        Integer anoLetivo,
        boolean ativa
) {}
