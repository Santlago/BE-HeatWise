package br.com.fiap.heatwise.model;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String apelido;

    @NotBlank
    @URL
    private String url;

    @ManyToOne
    // @JoinColumn(name = "empresa_id")
    private Empresa empresa;

}
