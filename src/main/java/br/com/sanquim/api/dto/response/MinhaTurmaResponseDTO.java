package br.com.sanquim.api.dto.response;

import java.util.List;

public record MinhaTurmaResponseDTO(
        Long atribuicaoId,
        Long turmaId,
        String turmaNome,
        String curso,
        String periodo,
        String local,
        Long disciplinaId,
        String disciplinaNome,
        Integer cargaHoraria,
        String professorNome,
        int totalAlunosMatriculados,
        List<AlunoResumoDTO> alunos
) {}
