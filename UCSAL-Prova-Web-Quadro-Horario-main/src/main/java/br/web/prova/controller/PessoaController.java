package br.web.prova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import br.web.prova.model.Pessoa;
import br.web.prova.model.AlunoDisciplinaInfoFormSubmission;
import br.web.prova.model.Disciplina;
import br.web.prova.repository.DisciplinaRepository;
import br.web.prova.repository.PessoaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PessoaController {
	@Autowired
	private PessoaRepository pr;

	@Autowired
	private DisciplinaRepository dr;

	@RequestMapping(value = "/pessoa/cadastro", method = RequestMethod.GET)
	private String form() {
		return "pessoas/formPessoa";
	}

	@RequestMapping(value = "/pessoa/cadastro", method = RequestMethod.POST)
	public String form(Pessoa pessoa) {
		pr.save(pessoa);
		return "redirect:/pessoa/showInfo?id=" + pessoa.getMatricula();
	}

	@RequestMapping(value = "/pessoa/info", method = RequestMethod.GET)
	private String form2() {
		return "pessoas/formInfoPessoa";
	}

	@RequestMapping(value = "/pessoa/info", method = RequestMethod.POST)
	public String form2(AlunoDisciplinaInfoFormSubmission submission, HttpServletResponse response) {
		return "redirect:/pessoa/showInfo?id=" + submission.getId();
	}

	@RequestMapping("/pessoa/showInfo")
	public ModelAndView pessoaInformacoes(HttpServletRequest request, HttpServletResponse response) {
		int attr = Integer.parseInt(request.getParameter("id"));
		// procure pela pessoa
		Pessoa pessoa = pr.findByMatricula(attr);
		if (pessoa == null) {
			writeError(response, "Pessoa não existe");
			return null;
		}
		ModelAndView mv = new ModelAndView("pessoas/infoPessoa");
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		List<Integer> pessoaDisciplinas = pessoa.getDisciplinas();
		for (Integer pessoaDisciplinaId: pessoaDisciplinas) {
			Disciplina pessoaDisciplina = dr.findById(pessoaDisciplinaId);
			if (pessoaDisciplina != null) {
				disciplinas.add(pessoaDisciplina);
			}
		}
		mv.addObject("pessoa", pessoa);
		mv.addObject("disciplinas", disciplinas);
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
