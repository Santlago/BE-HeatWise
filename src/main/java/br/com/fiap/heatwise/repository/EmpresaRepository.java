package br.com.fiap.heatwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.heatwise.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
