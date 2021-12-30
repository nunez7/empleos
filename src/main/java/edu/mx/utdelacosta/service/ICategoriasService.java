package edu.mx.utdelacosta.service;

import java.util.List;

import edu.mx.utdelacosta.model.Categoria;

public interface ICategoriasService {
	void guardar(Categoria categoria);

	List<Categoria> buscarTodas();

	Categoria buscarPorId(Integer idCategoria);
}