package edu.mx.utdelacosta.service;

import java.util.List;

import edu.mx.utdelacosta.model.Vacante;

public interface IVacanteService {
	
	List<Vacante> buscarTodas();
	
	Vacante buscarPorId(Integer id);

}
