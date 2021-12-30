package edu.mx.utdelacosta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.mx.utdelacosta.model.Categoria;
import edu.mx.utdelacosta.service.ICategoriasService;

@Controller
@RequestMapping(value = "/categorias")
public class CategoriasController {
	
	 @Autowired 
	 private ICategoriasService serviceCategorias;

	// @GetMapping("/index")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String mostrarIndex(Model model) {
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		return "categorias/listCategorias";
	}

	// @GetMapping("/create")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String crear() {
		return "categorias/formCategoria";
	}

	// @PostMapping("/save")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String guardar(Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "categorias/formCategoria";
		}
		serviceCategorias.guardar(categoria);
		attributes.addFlashAttribute("msg", "Registro Guardado");
		return "redirect:/categorias/index"; 
	}

}