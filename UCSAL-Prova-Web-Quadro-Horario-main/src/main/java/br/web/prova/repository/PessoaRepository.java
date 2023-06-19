package br.web.prova.repository;

import org.springframework.data.repository.CrudRepository;

import br.web.prova.model.Pessoa;

public interface PessoaRepository extends CrudRepository<Pessoa, String> {
    Pessoa findByMatricula(Integer id);
}
