package br.com.sanquim.api.dto.response;

import java.time.Instant;

public record LoginResponseDTO(
        String token,
        String tipoToken,
        long expiraEmSegundos,
        Instant expiraEm,
        String role,
        String nome,
        String email
) {}
