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

import br.com.fiap.heatwise.model.Site;
import br.com.fiap.heatwise.repository.SiteRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("site")
@Slf4j
public class SiteController {
    @Autowired // Injeção de Dependência - Inversão de Controle
    SiteRepository repository;

    @GetMapping
    public List<Site> index() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Site create(@RequestBody Site site) {
        log.info("Cadastrando site {}", site);
        return repository.save(site);
    }

    @GetMapping("{id}")
    public ResponseEntity<Site> show(@PathVariable Long id) {
        log.info("buscando site com id {}", id);

        return repository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("apagando site {}", id);
        verificarSeSiteExiste(id);
        repository.deleteById(id);
    }

    @PutMapping("{id}")
    public Site update(@PathVariable Long id, @RequestBody Site site) {
        log.info("atualizar site {} para {}", id, site);

        verificarSeSiteExiste(id);
        site.setId(id);
        return repository.save(site);
    }

    private void verificarSeSiteExiste(Long id) {
        repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "Não existe site com o id informado"));
    }
}
