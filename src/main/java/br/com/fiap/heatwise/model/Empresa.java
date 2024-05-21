package br.com.fiap.heatwise.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.heatwise.controller.EmpresaController;
import br.com.fiap.heatwise.validation.TipoPlano;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Empresa extends EntityModel<Empresa> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "{movimentacao.nome.notblank}")
    @Size(min = 1, max = 50)
    private String nome;

    @NotBlank
    // @CNPJ
    // @Column(unique = true)
    private String cnpj;

    @TipoPlano
    private Long idPlano;

    @NotBlank
    private String telefone;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    public EntityModel<Empresa> toEntityModel() {
        return EntityModel.of(
                this,
                linkTo(methodOn(EmpresaController.class).show(id)).withSelfRel(),
                linkTo(methodOn(EmpresaController.class).destroy(id)).withRel("delete"),
                linkTo(methodOn(EmpresaController.class).index()).withRel("contents"));
    }

    public EntityModel<Empresa> toModel() {
        return EntityModel.of(this,
                linkTo(methodOn(EmpresaController.class).index()).withRel("contents"));
    }
}