package br.com.sanquim.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TurmaRequestDTO(
        @NotBlank(message = "O nome da turma é obrigatório (ex: 1º Ano EM - Turma B).")
        String nome,

        @NotBlank(message = "O curso é obrigatório (ex: Ensino Médio, Pré-Vestibular).")
        String curso,

        String local,

        @NotBlank(message = "O período é obrigatório (ex: Matutino, Tarde, Integral, Noturno).")
        String periodo,

        Integer anoLetivo
) {}
