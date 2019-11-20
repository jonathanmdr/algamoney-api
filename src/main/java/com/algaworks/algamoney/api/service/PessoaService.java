package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizar(Long id, Pessoa pessoa) {
        Pessoa pessoaSalva = buscaPessoaPorId(id);

        pessoaSalva.getContatos().clear();
        pessoaSalva.getContatos().addAll(pessoa.getContatos());

        pessoaSalva.getContatos().forEach(contato -> contato.setPessoa(pessoaSalva));

        BeanUtils.copyProperties(pessoa, pessoaSalva, "id", "contatos");
        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
        Pessoa pessoaSalva = buscaPessoaPorId(id);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    private Pessoa buscaPessoaPorId(Long id) {
        Optional<Pessoa> pessoaSalva = pessoaRepository.findById(id);
        if (!pessoaSalva.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }
        return pessoaSalva.get();
    }

    public Pessoa salvar(Pessoa pessoa) {
        pessoa.getContatos().forEach(contato -> contato.setPessoa(pessoa));
        return pessoaRepository.save(pessoa);
    }

}
