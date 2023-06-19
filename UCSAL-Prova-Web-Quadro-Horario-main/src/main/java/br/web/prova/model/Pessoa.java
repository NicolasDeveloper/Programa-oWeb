package br.web.prova.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import br.web.prova.data.Conexao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Validated
public class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer matricula;
	private String nome;
	private String email;
	private Boolean isProfessor;
	private Integer numDisciplinas = 0;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date data;

	public Integer getMatricula() {
		return matricula;
	}

	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getNumDisciplinas() {
		return numDisciplinas;
	}

	public void setNumDisciplinas(Integer numDisciplinas) {
		this.numDisciplinas = numDisciplinas;
	}

	public void addNumDisciplinas() {
		numDisciplinas++;
	}

	public Boolean getIsProfessor() {
		if (isProfessor == null)
			return false;
		return isProfessor;
	}

	public void setIsProfessor(Boolean isProfessor) {
		this.isProfessor = isProfessor;
	}

	public List<Integer> getDisciplinas() {
		String query = String.format("SELECT disciplina.id FROM pessoa "
				+ "INNER JOIN disciplina_has_alunos ON pessoa.matricula = disciplina_has_alunos.alunos_matricula "
				+ "INNER JOIN disciplina ON disciplina.id = disciplina_has_alunos.disciplina_id "
				+ "WHERE pessoa.matricula = %d;", matricula);

		List<Integer> listaDisciplinas = new ArrayList<Integer>();

		ResultSet result = Conexao.executeQuery(query);
		try {
			while (result.next()) {
				int id = result.getInt(1);
				listaDisciplinas.add(id);
			}
		} catch (SQLException e) {
		}

		return listaDisciplinas;
	}
}
