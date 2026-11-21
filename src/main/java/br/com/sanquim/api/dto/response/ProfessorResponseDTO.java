package br.com.sanquim.api.dto.response;

public record ProfessorResponseDTO(
        Long id,
        Long usuarioId,
        String nome,
        String cpf,
        String rg,
        String endereco,
        String bairro,
        String cidade,
        String fone,
        String email,
        boolean ativo
) {}
