package br.com.sanquim.api.dto.response;

import java.time.LocalDate;

public record ChamadaResponseDTO(
        Long turmaId,
        String turmaNome,
        Long disciplinaId,
        String disciplinaNome,
        LocalDate dataAula,
        int totalRegistros,
        int totalPresentes,
        int totalAusentes,
        String mensagem
) {}
