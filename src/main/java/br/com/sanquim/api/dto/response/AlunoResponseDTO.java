package br.com.sanquim.api.dto.response;

import java.time.LocalDate;

public record AlunoResponseDTO(
        Long id,
        Long usuarioId,
        String nome,
        String ra,
        LocalDate dataNascimento,
        String cpf,
        String rg,
        String endereco,
        String bairro,
        String cidade,
        String fone,
        String email,
        boolean ativo
) {}
