package br.com.fiap.heatwise.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("site")
@Slf4j
@CacheConfig(cacheNames = "sites")
@Tag(name = "Sites")
public class SiteController {
    @Autowired // Injeção de Dependência - Inversão de Controle
    SiteRepository repository;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar todos os sites", description = "Retorna um array com todos os sites no formato objeto")
    public List<Site> index() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar site", description = "Cria um novo site com os dados enviados no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Site cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição", useReturnTypeSchema = false)
    })
    public Site create(@RequestBody Site site) {
        log.info("Cadastrando site {}", site);
        return repository.save(site);
    }

    @GetMapping("{id}")
    @Operation(summary = "Obter detalhes de um site", description = "Retorna os detalhes de um site específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Site encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Site não encontrado")
    })
    public ResponseEntity<Site> show(@PathVariable Long id) {
        log.info("Buscando site com id {}", id);

        return repository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar um site", description = "Remove um site específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Site deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Site não encontrado")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Apagando site {}", id);
        verificarSeSiteExiste(id);
        repository.deleteById(id);
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar um site", description = "Atualiza os dados de um site específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Site atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Site não encontrado")
    })
    public Site update(@PathVariable Long id, @RequestBody Site site) {
        log.info("Atualizar site {} para {}", id, site);

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
