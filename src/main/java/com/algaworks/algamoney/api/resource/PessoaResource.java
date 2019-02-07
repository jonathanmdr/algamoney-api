package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPeloId(@PathVariable Long id) {
        Pessoa pessoa = pessoaRepository.findOne(id);

        return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        pessoaRepository.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaService.atualizar(id, pessoa);
        return ResponseEntity.ok(pessoaSalva);
    }

}
