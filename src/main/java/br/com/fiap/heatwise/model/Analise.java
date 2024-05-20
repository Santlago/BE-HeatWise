package br.com.fiap.heatwise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Analise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String relatorio;
    
    @NotNull
    private String dashboard;
    
    @NotNull
    private String mapaCalor;

    @OneToOne
    // @JoinColumn(name = "site_id")
    private Site site;

}
