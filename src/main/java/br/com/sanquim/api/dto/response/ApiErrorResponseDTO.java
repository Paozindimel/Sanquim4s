package br.com.sanquim.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        List<CampoInvalidoDTO> camposInvalidos
) {
    public record CampoInvalidoDTO(String campo, String mensagem) {}
}
