package br.web.prova.repository;

import org.springframework.data.repository.CrudRepository;

import br.web.prova.model.Disciplina;

public interface DisciplinaRepository extends CrudRepository<Disciplina, String> {
	Disciplina findById(Integer id);
}
