package br.com.sanquim.api.controller;

import br.com.sanquim.api.dto.request.*;
import br.com.sanquim.api.dto.response.*;
import br.com.sanquim.api.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "2. Gestão Administrativa (/api/admin)", description = "Exclusivo ROLE_GESTAO — Cadastros estruturais e enturmação")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/professores")
    @Operation(summary = "Cadastra um novo professor com acesso ROLE_PROFESSOR")
    public ResponseEntity<ProfessorResponseDTO> cadastrarProfessor(@Valid @RequestBody ProfessorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.cadastrarProfessor(dto));
    }

    @GetMapping("/professores")
    @Operation(summary = "Lista todos os professores cadastrados")
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessores() {
        return ResponseEntity.ok(adminService.listarProfessores());
    }

    @PostMapping("/alunos")
    @Operation(summary = "Cadastra um novo aluno com acesso ROLE_ALUNO")
    public ResponseEntity<AlunoResponseDTO> cadastrarAluno(@Valid @RequestBody AlunoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.cadastrarAluno(dto));
    }

    @GetMapping("/alunos")
    @Operation(summary = "Lista todos os alunos cadastrados")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos() {
        return ResponseEntity.ok(adminService.listarAlunos());
    }

    @PostMapping("/turmas")
    @Operation(summary = "Cria uma nova turma letiva")
    public ResponseEntity<TurmaResponseDTO> criarTurma(@Valid @RequestBody TurmaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.criarTurma(dto));
    }

    @GetMapping("/turmas")
    @Operation(summary = "Lista todas as turmas letivas")
    public ResponseEntity<List<TurmaResponseDTO>> listarTurmas() {
        return ResponseEntity.ok(adminService.listarTurmas());
    }

    @PostMapping("/disciplinas")
    @Operation(summary = "Cadastra uma nova disciplina (e opcionalmente já atribui turma e professor)")
    public ResponseEntity<DisciplinaResponseDTO> criarDisciplina(@Valid @RequestBody DisciplinaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.criarDisciplina(dto));
    }

    @GetMapping("/disciplinas")
    @Operation(summary = "Lista todas as disciplinas cadastradas")
    public ResponseEntity<List<DisciplinaResponseDTO>> listarDisciplinas() {
        return ResponseEntity.ok(adminService.listarDisciplinas());
    }

    @PostMapping("/atribuicoes")
    @Operation(summary = "Vincula um professor titular a uma disciplina em uma turma")
    public ResponseEntity<AtribuicaoResponseDTO> atribuirProfessor(@Valid @RequestBody AtribuicaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.atribuirProfessor(dto));
    }

    @PostMapping("/matriculas")
    @Operation(summary = "Realiza o vínculo de enturmação (associa aluno a uma turma ativa)")
    public ResponseEntity<MatriculaResponseDTO> matricularAluno(@Valid @RequestBody MatriculaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.matricularAluno(dto));
    }

    @GetMapping("/matriculas")
    @Operation(summary = "Lista todas as matrículas realizadas")
    public ResponseEntity<List<MatriculaResponseDTO>> listarMatriculas() {
        return ResponseEntity.ok(adminService.listarMatriculas());
    }
}
