package edu.mx.utdelacosta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.mx.utdelacosta.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {

}