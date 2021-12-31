package edu.mx.utdelacosta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.mx.utdelacosta.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

}
