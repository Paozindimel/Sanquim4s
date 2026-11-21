package br.com.sanquim.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "atribuicoes_aula", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"turma_id", "disciplina_id"})
})
public class AtribuicaoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    public AtribuicaoAula() {
    }

    public AtribuicaoAula(Professor professor, Turma turma, Disciplina disciplina) {
        this.professor = professor;
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }
}
