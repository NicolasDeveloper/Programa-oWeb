package br.web.prova.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.web.prova.model.AlunoDisciplinaInfoFormSubmission;
import br.web.prova.model.AlunoMatriculaFormSubmission;
import br.web.prova.model.Disciplina;
import br.web.prova.model.Pessoa;
import br.web.prova.repository.DisciplinaRepository;
import br.web.prova.repository.PessoaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DisciplinaController {
	@Autowired
	private DisciplinaRepository dr;

	@Autowired
	private PessoaRepository pr;

	@RequestMapping(value = "/disciplina/cadastro", method = RequestMethod.GET)
	private String form() {
		return "disciplinas/formDisciplina";
	}

	@RequestMapping(value = "/disciplina/cadastro", method = RequestMethod.POST)
	public String form(Disciplina disciplina, HttpServletResponse response) {
		// procure pelo professor
		Pessoa professor = disciplina.getProfessor();
		if (professor == null) {
			writeError(response, "Professor não existe");
			return "redirect:/disciplinas/cadastro";
		}

		// verifique se é um professor
		if (!professor.getIsProfessor()) {
			writeError(response, "Não é um professor");
			return "redirect:/disciplinas/cadastro";
		}

		dr.save(disciplina);

		return "redirect:/disciplina/showInfo?id=" + disciplina.getId();
	}

	@RequestMapping(value = "/disciplina/matricularAluno", method = RequestMethod.GET)
	private String form2() {
		return "disciplinas/formAlunoMatricula";
	}

	@RequestMapping(value = "/disciplina/matricularAluno", method = RequestMethod.POST)
	public String form2(AlunoMatriculaFormSubmission submission, HttpServletResponse response) {
		// procure pela disciplina
		Disciplina disciplina = dr.findById(submission.getCodigoDisciplina());
		if (disciplina == null) {
			writeError(response, "Disciplina não existe");
			return "redirect:/disciplinas/matricularAluno";
		}

		// procure pelo aluno
		Pessoa aluno = pr.findByMatricula(submission.getCodigoEstudante());
		if (aluno == null) {
			writeError(response, "Aluno não existe");
			return "redirect:/disciplinas/matricularAluno";
		}

		// verifique máximo de alunos
		if (disciplina.getAlunos().size() == 20) {
			writeError(response, "Disciplina já contém o máximo de alunos (20)");
			return "redirect:/disciplinas/matricularAluno";
		}

		// verifique se já contém o aluno
		if (disciplina.hasAluno(aluno)) {
			writeError(response, "Disciplina já tem esse aluno");
			return "redirect:/disciplinas/matricularAluno";
		}

		// verifique se é um professor
		if (aluno.getIsProfessor()) {
			writeError(response, "Não é possível matricular um professor");
			return "redirect:/disciplinas/matricularAluno";
		}

		// verifique se o aluno já tem 10 disciplinas
		if (aluno.getNumDisciplinas() == 10) {
			writeError(response, "Aluno já contem o máximo de disciplinas (10)");
			return "redirect:/disciplinas/matricularAluno";
		} else
			aluno.addNumDisciplinas();

		disciplina.addAluno(aluno);
		dr.save(disciplina);

		return "redirect:/disciplina/showInfo?id=" + submission.getCodigoDisciplina();
	}

	@RequestMapping(value = "/disciplina/info", method = RequestMethod.GET)
	private String form3() {
		return "disciplinas/formInfoDisciplina";
	}

	@RequestMapping(value = "/disciplina/info", method = RequestMethod.POST)
	public String form3(AlunoDisciplinaInfoFormSubmission submission, HttpServletResponse response) {
		return "redirect:/disciplina/showInfo?id=" + submission.getId();
	}

	@RequestMapping("/disciplina/showInfo")
	public ModelAndView disciplinaInformacoes(HttpServletRequest request, HttpServletResponse response) {
		int attr = Integer.parseInt(request.getParameter("id"));
		// procure pela disciplina
		Disciplina disciplina = dr.findById(attr);
		if (disciplina == null) {
			writeError(response, "Disciplina não existe");
			return null;
		}
		ModelAndView mv = new ModelAndView("disciplinas/infoDisciplina");
		mv.addObject("disciplina", disciplina);
		return mv;
	}

	private void writeError(HttpServletResponse response, String erro) {
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println(erro);
			writer.close();
		} catch (IOException e) {

		}
	}
}
