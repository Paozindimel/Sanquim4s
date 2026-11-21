package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AlunoRequestDTO(
        @NotBlank(message = "O nome do aluno é obrigatório.")
        String nome,

        @NotBlank(message = "O RA do aluno é obrigatório.")
        String ra,

        LocalDate dataNascimento,

        @NotBlank(message = "O CPF do aluno é obrigatório.")
        String cpf,

        String rg,
        String endereco,
        String bairro,
        String cidade,
        String fone,

        @NotBlank(message = "O e-mail do aluno é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotBlank(message = "A senha inicial de acesso é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha
) {}
