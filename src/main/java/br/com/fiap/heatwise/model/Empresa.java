package br.com.fiap.heatwise.model;

import java.util.List;

import org.hibernate.validator.constraints.br.CNPJ;

import br.com.fiap.heatwise.validation.TipoPlano;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Empresa {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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
}