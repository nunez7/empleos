package edu.mx.utdelacosta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.mx.utdelacosta.model.Usuario;
import edu.mx.utdelacosta.service.IUsuariosService;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private IUsuariosService serviceUsuarios;
    
    @GetMapping("/index")
	public String mostrarIndex(Model model) {
    	// Ejercicio
    	model.addAttribute("usuarios", serviceUsuarios.buscarTodos());
    	return "usuarios/listUsuarios";
	}
    
    @GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {		    	
    	// Ejercicio.
    	serviceUsuarios.eliminar(idUsuario);
    	attributes.addFlashAttribute("msg", "El usuario fue eliminado");
		return "redirect:/usuarios/index";
	}
    
    @GetMapping("/block/{id}")
   	public String bloquearUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {		    	
       	// Ejercicio.
    	Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
    	if(usuario.getEstatus()==0) {
           	usuario.setEstatus(1);
           	attributes.addFlashAttribute("msg", "El usuario fue activado");
    	}else {
           	usuario.setEstatus(0);
           	attributes.addFlashAttribute("msg", "El usuario fue bloqueado");
    	}
       	serviceUsuarios.guardar(usuario);
   		return "redirect:/usuarios/index";
   	}
}
