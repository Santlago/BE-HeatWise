package br.com.fiap.heatwise.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.List;
import java.util.Optional;

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

import br.com.fiap.heatwise.model.Empresa;
import br.com.fiap.heatwise.repository.EmpresaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("empresa")
@Slf4j
@CacheConfig(cacheNames = "empresas")
public class EmpresaController {

    @Autowired // Injeção de Dependência - Inversão de Controle
    EmpresaRepository repository;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar todas as empresas", description = "Retorna um array com todas as empresas no formato objeto")
    public List<Empresa> index() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar empresa", description = "Cria uma nova empresa com os dados enviados no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados enviados são inválidos. Verifique o corpo da requisição", useReturnTypeSchema = false)
    })
    public Empresa create(@RequestBody Empresa empresa) {
        log.info("Cadastrando empresa {}", empresa);
        return repository.save(empresa);
    }

    @GetMapping("{id}")
    @Operation(summary = "Obter detalhes de uma empresa", description = "Retorna os detalhes de uma empresa específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<Empresa> show(@PathVariable Long id) {
        log.info("Buscando empresa com id {}", id);

        return repository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar uma empresa", description = "Remove uma empresa específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Apagando empresa {}", id);
        verificarSeEmpresaExiste(id);
        repository.deleteById(id);
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar uma empresa", description = "Atualiza os dados de uma empresa específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public Empresa update(@PathVariable Long id, @RequestBody Empresa empresa) {
        log.info("Atualizar empresa {} para {}", id, empresa);

        verificarSeEmpresaExiste(id);
        empresa.setId(id);
        return repository.save(empresa);
    }

    @PostMapping("/login")
    @Operation(summary = "Login de empresa", description = "Autentica uma empresa com email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
    })
    public ResponseEntity<?> login(@RequestBody Empresa loginRequest) {
        log.info("Tentativa de login para {}", loginRequest.getEmail());

        Optional<Empresa> empresa = repository.findByEmail(loginRequest.getEmail());

        if (empresa.isPresent() && empresa.get().getSenha().equals(loginRequest.getSenha())) {
            return ResponseEntity.ok(empresa.get());
        } else {
            return ResponseEntity.status(UNAUTHORIZED).body("Email ou senha inválidos");
        }
    }

    private void verificarSeEmpresaExiste(Long id) {
        repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "Não existe empresa com o id informado"));
    }
}
