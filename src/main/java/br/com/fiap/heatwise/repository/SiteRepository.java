package br.com.fiap.heatwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.heatwise.model.Site;

public interface SiteRepository extends JpaRepository<Site, Long> {

}
