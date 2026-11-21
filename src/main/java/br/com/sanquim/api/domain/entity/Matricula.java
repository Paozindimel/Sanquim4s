package br.com.sanquim.api.domain.entity;

import br.com.sanquim.api.domain.enums.SituacaoMatricula;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "matriculas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_id", "turma_id"})
})
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private SituacaoMatricula situacao = SituacaoMatricula.ATIVA;

    public Matricula() {
    }

    public Matricula(Aluno aluno, Turma turma) {
        this.aluno = aluno;
        this.turma = turma;
        this.dataMatricula = LocalDate.now();
        this.situacao = SituacaoMatricula.ATIVA;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }
    public SituacaoMatricula getSituacao() { return situacao; }
    public void setSituacao(SituacaoMatricula situacao) { this.situacao = situacao; }
}
