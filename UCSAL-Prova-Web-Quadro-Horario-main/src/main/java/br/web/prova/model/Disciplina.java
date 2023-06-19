package br.web.prova.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Validated
@Table(name = "disciplina")
public class Disciplina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String nome;
	private Pessoa professor;
	private boolean horario1;
	private boolean horario2;

	@ManyToMany(fetch = FetchType.EAGER)
	@CollectionTable(name = "disciplina_has_alunos", joinColumns = @JoinColumn(name = "disciplina_id"))
	@Column(name = "aluno")
	private List<Pessoa> alunos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Pessoa getProfessor() {
		return professor;
	}

	public void setProfessor(Pessoa professor) {
		this.professor = professor;
	}

	public boolean isHorario1() {
		return horario1;
	}

	public void setHorario1(boolean horario1) {
		this.horario1 = horario1;
	}

	public boolean isHorario2() {
		return horario2;
	}

	public void setHorario2(boolean horario2) {
		this.horario2 = horario2;
	}

	public void addAluno(Pessoa aluno) {
		alunos.add(aluno);
	}

	public boolean hasAluno(Pessoa aluno) {
		for (Pessoa aluno2 : alunos) {
			if (aluno2 == aluno)
				return true;
		}
		return false;
	}

	public List<Pessoa> getAlunos() {
		return new ArrayList<Pessoa>(alunos);
	}
}
