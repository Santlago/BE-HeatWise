package br.com.fiap.heatwise.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.heatwise.model.Empresa;
import br.com.fiap.heatwise.repository.EmpresaRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("empresa")
@Slf4j
public class EmpresaController {
    
    @Autowired // Injeção de Dependência - Inversão de Controle
    EmpresaRepository repository;

    @GetMapping
    public List<Empresa> index() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Empresa create(@RequestBody Empresa empresa) {
        log.info("Cadastrando empresa {}", empresa);
        return repository.save(empresa);
    }

    @GetMapping("{id}")
    public ResponseEntity<Empresa> show(@PathVariable Long id) {
        log.info("buscando empresa com id {}", id);

        return repository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("apagando empresa {}", id);
        verificarSeEmpresaExiste(id);
        repository.deleteById(id);
    }

    @PutMapping("{id}")
    public Empresa update(@PathVariable Long id, @RequestBody Empresa empresa) {
        log.info("atualizar empresa {} para {}", id, empresa);

        verificarSeEmpresaExiste(id);
        empresa.setId(id);
        return repository.save(empresa);
    }

    private void verificarSeEmpresaExiste(Long id) {
        repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "Não existe empresa com o id informado"));
    }
}
