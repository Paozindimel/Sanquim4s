package br.com.sanquim.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_turma", nullable = false, unique = true, length = 80)
    private String nome;

    @Column(nullable = false, length = 60)
    private String curso;

    @Column(length = 40)
    private String local;

    @Column(nullable = false, length = 30)
    private String periodo;

    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo = 2026;

    @Column(nullable = false)
    private boolean ativa = true;

    public Turma() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public Integer getAnoLetivo() { return anoLetivo; }
    public void setAnoLetivo(Integer anoLetivo) { this.anoLetivo = anoLetivo; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}
