package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.persistence.model.Comentario;
import com.example.demo.persistence.model.Usuario;
import com.example.demo.persistence.model.enums.RolUsuario;
import com.example.demo.services.comentServices.ComentarioServicioI;
import com.example.demo.services.userServices.UsuarioServicioImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private UsuarioServicioImpl usuarioService;

	private ComentarioServicioI comentarioService;

	private PasswordEncoder passwordEncoder;

	static final String ADMIN_CONTENT_TEXT = "adminContent";
	static final String URL_ADMIN_HOME = "auth/admin/home";
	static final String REDIRECT_USUARIOS = "redirect:/admin/usuarios";
	static final String USUARIOS_TEXT = "usuarios";

	/**
	 * Constructor de la clase AdminController.
	 *
	 * @param usuarioService    Servicio de Usuario
	 * @param comentarioService Servicio de Comentario
	 * @param passwordEncoder   Encoder de contraseñas
	 */
	public AdminController(UsuarioServicioImpl usuarioService, ComentarioServicioI comentarioService,
			PasswordEncoder passwordEncoder) {
		this.usuarioService = usuarioService;
		this.comentarioService = comentarioService;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Maneja las solicitudes GET a "/admin/home".
	 *
	 * @param model Modelo utilizado para agregar atributos
	 * @return La vista de inicio de administrador
	 */
	@GetMapping("/home")
	@PreAuthorize("hasRole('ADMIN')")
	public String home(Model model) {
		return URL_ADMIN_HOME;
	}

	/**
	 * Maneja las solicitudes GET a "/admin/usuarios".
	 *
	 * @param model Modelo utilizado para agregar atributos
	 * @return La vista de administración de usuarios
	 */
	@GetMapping("/usuarios")
	@PreAuthorize("hasRole('ADMIN')")
	public String usuarios(Model model) {
		model.addAttribute(ADMIN_CONTENT_TEXT, "fragments/admin-usuarios");
		List<Usuario> usuarios = usuarioService.listarTodosLosUsuariosConRolUser();
		model.addAttribute(USUARIOS_TEXT, usuarios);
		return URL_ADMIN_HOME;
	}

	/**
	 * Maneja las solicitudes GET a "/admin/comentarios".
	 *
	 * @param model Modelo utilizado para agregar atributos
	 * @return La vista de administración de comentarios
	 */
	@GetMapping("/comentarios")
	@PreAuthorize("hasRole('ADMIN')")
	public String comentarios(Model model) {
		model.addAttribute(ADMIN_CONTENT_TEXT, "fragments/admin-comentarios");
		List<Comentario> comentarios = comentarioService.obtenerTodosLosComentarios();
		model.addAttribute("comentarios", comentarios);
		return URL_ADMIN_HOME;
	}

	/**
	 * Maneja las solicitudes GET a "/admin/estadisticas".
	 *
	 * @param model Modelo utilizado para agregar atributos
	 * @return La vista de estadísticas de administrador
	 */
	@GetMapping("/estadisticas")
	@PreAuthorize("hasRole('ADMIN')")
	public String estadisticas(Model model) {
		model.addAttribute(ADMIN_CONTENT_TEXT, "fragments/admin-estadisticas");
		model.addAttribute(USUARIOS_TEXT, usuarioService.listarTodosLosUsuarios());
		model.addAttribute("usuariosUser", usuarioService.listarTodosLosUsuariosConRolUser());
		model.addAttribute("usuariosAdmin", usuarioService.listarTodosLosUsuariosConRolAdmin());
		model.addAttribute("comentarios", comentarioService.obtenerTodosLosComentarios());
		return URL_ADMIN_HOME;
	}

	/**
	 * Maneja las solicitudes GET a "/admin/eliminarUsuario/{id}" para eliminar un
	 * usuario.
	 *
	 * @param id Identificador del usuario a eliminar
	 * @return Redirige a la página de administración de usuarios
	 */
	@GetMapping("/eliminarUsuario/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String eliminarUsuario(@PathVariable Long id) {
		usuarioService.eliminarUsuario(id);
		return REDIRECT_USUARIOS;
	}

	/**
	 * Maneja las solicitudes POST a "/admin/anadirUsuario" para agregar un usuario.
	 *
	 * @param model    Modelo utilizado para agregar atributos
	 * @param username Nombre de usuario del nuevo usuario
	 * @param password Contraseña del nuevo usuario
	 * @return Redirige a la página de administración de usuarios
	 */
	@PostMapping("/anadirUsuario")
	@PreAuthorize("hasRole('ADMIN')")
	public String anadirUsuario(Model model, @RequestParam String username, @RequestParam String password) {
		Usuario usuario = new Usuario();
		if (!usuarioService.obtenerPorUsername(username).isPresent()) {
			usuario.setUsername(username);
			usuario.setPassword(passwordEncoder.encode(password));
			usuario.getRoles().add(RolUsuario.ROLE_USER);
			usuarioService.añadirUsuario(usuario);
		}

		model.addAttribute(USUARIOS_TEXT, usuarioService.listarTodosLosUsuariosConRolUser());
		return REDIRECT_USUARIOS;
	}

	/**
	 * Maneja las solicitudes POST a "/admin/editarUsuario/{id}" para editar un
	 * usuario.
	 *
	 * @param id       Identificador del usuario a editar
	 * @param username Nuevo nombre de usuario
	 * @param password Nueva contraseña del usuario
	 * @return Redirige a la página de administración de usuarios
	 */
	@PostMapping("/editarUsuario/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String editarUsuario(@PathVariable Long id, @RequestParam String username, @RequestParam String password) {
		Optional<Usuario> usuarioOpt = Optional.ofNullable(usuarioService.obtenerPorId(id));

		if (usuarioOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();
			usuario.setUsername(username);

			usuario.setPassword(passwordEncoder.encode(password));

			// Guardar los cambios
			usuarioService.guardar(usuario);
		}

		return REDIRECT_USUARIOS;
	}

	/**
	 * Maneja las solicitudes GET a "/admin/eliminarComentario/{id}" para eliminar
	 * un comentario.
	 *
	 * @param id Identificador del comentario a eliminar
	 * @return Redirige a la página de administración de comentarios
	 */
	@GetMapping("eliminarComentario/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String eliminarComentario(@PathVariable Long id) {
		comentarioService.eliminar(id);

		return "redirect:/admin/comentarios";
	}
}
