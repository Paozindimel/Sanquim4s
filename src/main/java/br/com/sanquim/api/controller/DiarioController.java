package br.com.sanquim.api.controller;

import br.com.sanquim.api.dto.request.*;
import br.com.sanquim.api.dto.response.*;
import br.com.sanquim.api.service.DiarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diario")
@Tag(name = "3. Pedagógico e Diário de Classe (/api/diario)", description = "ROLE_PROFESSOR e ROLE_GESTAO — Chamada em lote, avaliações, notas e planos de aula")
public class DiarioController {

    private final DiarioService diarioService;

    public DiarioController(DiarioService diarioService) {
        this.diarioService = diarioService;
    }

    @GetMapping("/minhas-turmas")
    @Operation(summary = "Lista turmas, disciplinas e alunos vinculados ao professor autenticado")
    public ResponseEntity<List<MinhaTurmaResponseDTO>> listarMinhasTurmas() {
        return ResponseEntity.ok(diarioService.listarMinhasTurmas());
    }

    @PostMapping("/chamada")
    @Operation(summary = "Registra presença e ausência em lote para um dia de aula")
    public ResponseEntity<ChamadaResponseDTO> registrarChamada(@Valid @RequestBody ChamadaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diarioService.registrarChamada(dto));
    }

    @PostMapping("/avaliacoes")
    @Operation(summary = "Cadastra uma atividade avaliativa (prova, seminário, trabalho) com peso e nota máxima")
    public ResponseEntity<AvaliacaoResponseDTO> cadastrarAvaliacao(@Valid @RequestBody AvaliacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diarioService.cadastrarAvaliacao(dto));
    }

    @GetMapping("/avaliacoes")
    @Operation(summary = "Lista avaliações cadastradas")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoes() {
        return ResponseEntity.ok(diarioService.listarAvaliacoes());
    }

    @PostMapping("/avaliacoes/{avaliacaoId}/notas")
    @Operation(summary = "Lança ou atualiza notas dos alunos na avaliação informada")
    public ResponseEntity<List<NotaResponseDTO>> lancarNotas(
            @PathVariable Long avaliacaoId,
            @Valid @RequestBody LancamentoNotasRequestDTO dto) {
        return ResponseEntity.ok(diarioService.lancarOuAtualizarNotas(avaliacaoId, dto));
    }

    @PostMapping("/planos-aula")
    @Operation(summary = "Registra planejamento e resumo dos tópicos lecionados na data")
    public ResponseEntity<PlanoAulaResponseDTO> registrarPlanoAula(@Valid @RequestBody PlanoAulaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diarioService.registrarPlanoAula(dto));
    }
}
