package edu.mx.utdelacosta.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.mx.utdelacosta.model.Rol;
import edu.mx.utdelacosta.model.Usuario;
import edu.mx.utdelacosta.model.Vacante;
import edu.mx.utdelacosta.service.IUsuariosService;
import edu.mx.utdelacosta.service.IVacantesService;

@Controller
public class HomeController {
	//Inyectando instancias
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	@GetMapping("/tabla")
	public String mostrarTabla(Model model) {
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("vacantes", lista);	
		return "tabla";
	}
	
	@GetMapping("/detalle")
	public String mostrarDetalle(Model model) {
		Vacante vacante = new Vacante();
		vacante.setNombre("Ingeniero de comunicaciones");
		vacante.setDescripcion("Se solicita ingeniero para dar soporte a intranet");
		vacante.setFecha(new Date());
		vacante.setSalario(9700.0);
		model.addAttribute("vacante", vacante);
		return "detalle";
	}
	
	@GetMapping("/listado")
	public String mostrarListado(Model model) {
		List<String> lista = new LinkedList<String>();
		lista.add("Ingeniero  de Sistemas");
		lista.add("Auxiliar de Contabilidad");
		lista.add("Vendedor");
		lista.add("Arquitecto");
		
		model.addAttribute("empleos", lista);
		
		return "listado";
	}

	@GetMapping("/")
	public String mostrarHome(Model model) {
		return "home";
	}
	
	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "formRegistro";
	}
	
	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {

		// Ejercicio.
		usuario.setEstatus(1); // Activado por defecto
		usuario.setFechaRegistro(new Date()); // Fecha de Registro, la fecha actual del servidor
		// Creamos el Perfil que le asignaremos al usuario nuevo
		Rol rol = new Rol();
		rol.setId(3); // Perfil USUARIO
		usuario.agregar(rol);
		/**
		 * Guardamos el usuario en la base de datos. El Perfil se guarda automaticamente
		 */
		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "El registro fue guardado correctamente!");
		return "redirect:/usuarios/index";
	}
	
	//Estos atributos estaran disponibles a todos los metodos
	@ModelAttribute
	public void setGenericos(Model model) {
		/*Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		model.addAttribute("search",vacanteSearch);*/
		model.addAttribute("vacantes", serviceVacantes.buscarDestacadas());
	}
}
