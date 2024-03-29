package edu.mx.utdelacosta.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.mx.utdelacosta.model.Rol;
import edu.mx.utdelacosta.model.Usuario;
import edu.mx.utdelacosta.model.Vacante;
import edu.mx.utdelacosta.service.ICategoriasService;
import edu.mx.utdelacosta.service.IUsuariosService;
import edu.mx.utdelacosta.service.IVacantesService;

@Controller
public class HomeController {
	// Inyectando instancias
	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	private IUsuariosService serviceUsuarios;

	@Autowired
	private ICategoriasService serviceCategorias;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Método que esta mapeado al botón Ingresar en el menú
	 * @param authentication
	 * @param session
	 * @return
	 */
	@GetMapping("/index")
	public String mostrarIndex(Authentication authentication, HttpSession session) {		
		
		// Como el usuario ya ingreso, ya podemos agregar a la session el objeto usuario.
		String username = authentication.getName();		
		
		for(GrantedAuthority rol: authentication.getAuthorities()) {
			System.out.println("ROL: " + rol.getAuthority());
		}
		
		if (session.getAttribute("usuario") == null){
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);	
			//System.out.println("Usuario: " + usuario);
			session.setAttribute("usuario", usuario);
		}
		
		return "redirect:/";
	}

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
		List<String> lista = new LinkedList<>();
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
		// Recuperamos el password en texto plano
		String pwdPlano = usuario.getPassword();
		// Encriptamos el pwd BCryptPasswordEncoder
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		// Hacemos un set al atributo password (ya viene encriptado)
		usuario.setPassword(pwdEncriptado);
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

	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		// System.out.println("buscando por: "+vacante);
		// En el buscador se usa like en lugar de =
		ExampleMatcher martcher = ExampleMatcher.matching().withMatcher("descripcion",
				ExampleMatcher.GenericPropertyMatchers.contains());

		Example<Vacante> example = Example.of(vacante, martcher);
		List<Vacante> lista = serviceVacantes.buscarByExample(example);
		model.addAttribute("vacantes", lista);
		return "home";
	}
	
	/**
	 * Metodo que muestra la vista de la pagina de Acerca
	 * @return
	 */
	@GetMapping("/about")
	public String mostrarAcerca() {			
		return "acerca";
	}

	/**
	 * Método personalizado para cerrar la sesión del usuario
	 * @param request
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}

	// Estos atributos estaran disponibles a todos los metodos
	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();
		// Resetea la imagen a null para que no envie datos para busqueda
		vacanteSearch.reset();
		model.addAttribute("search", vacanteSearch);
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		model.addAttribute("vacantes", serviceVacantes.buscarDestacadas());
	}

	/*
	 * InitBinder para String, si los detecta vacios en el Data Binding los settea a
	 * NULL
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	/**
	 * Método que muestra el formulario de login personalizado.
	 * @return
	 */
	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}
	
	/**
     * Utileria para encriptar texto con el algorito BCrypt
     * @param texto
     * @return
     */
    @GetMapping("/bcrypt/{texto}")
    @ResponseBody
   	public String encriptar(@PathVariable("texto") String texto) {    	
   		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
   	}
}
