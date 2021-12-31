package edu.mx.utdelacosta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.mx.utdelacosta.model.Vacante;

public interface VacantesRepository extends JpaRepository<Vacante, Integer> {

}
