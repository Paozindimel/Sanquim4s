package br.com.sanquim.api.dto.response;

import java.time.LocalDate;

public record PlanoAulaResponseDTO(
        Long id,
        Long turmaId,
        String turmaNome,
        Long disciplinaId,
        String disciplinaNome,
        String professorNome,
        LocalDate dataAula,
        String titulo,
        String conteudo,
        String objetivos
) {}
