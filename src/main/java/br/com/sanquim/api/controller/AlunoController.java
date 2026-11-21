package br.com.sanquim.api.controller;

import br.com.sanquim.api.dto.response.BoletimResponseDTO;
import br.com.sanquim.api.dto.response.FrequenciaAlunoResponseDTO;
import br.com.sanquim.api.dto.response.PlanoAulaResponseDTO;
import br.com.sanquim.api.service.AlunoPortalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/aluno")
@Tag(name = "4. Portal do Aluno (/api/aluno)", description = "Exclusivo ROLE_ALUNO — Consulta de boletim, frequência e linha do tempo de aulas")
public class AlunoController {

    private final AlunoPortalService alunoPortalService;

    public AlunoController(AlunoPortalService alunoPortalService) {
        this.alunoPortalService = alunoPortalService;
    }

    @GetMapping("/boletim")
    @Operation(summary = "Consulta discriminada de notas obtidas por disciplina e média parcial ponderada")
    public ResponseEntity<BoletimResponseDTO> consultarBoletim() {
        return ResponseEntity.ok(alunoPortalService.consultarBoletim());
    }

    @GetMapping("/frequencia")
    @Operation(summary = "Consulta percentual global de frequência e total de faltas por disciplina")
    public ResponseEntity<FrequenciaAlunoResponseDTO> consultarFrequencia() {
        return ResponseEntity.ok(alunoPortalService.consultarFrequencia());
    }

    @GetMapping("/aulas")
    @Operation(summary = "Consulta a linha do tempo de conteúdos e aulas lecionadas na turma do aluno")
    public ResponseEntity<List<PlanoAulaResponseDTO>> consultarAulas() {
        return ResponseEntity.ok(alunoPortalService.consultarLinhaDoTempoAulas());
    }
}
