package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.persistence.model.Comentario;
import com.example.demo.persistence.model.Usuario;
import com.example.demo.services.comentServices.ComentarioServicioI;
import com.example.demo.services.userServices.UsuarioServicioImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	ComentarioServicioI comentariosServicio;
	UsuarioServicioImpl usuarioServicio;

	/**
	 * Constructor de la clase UserController.
	 *
	 * @param comentariosServicio Servicio de Comentarios
	 * @param usuarioServicio     Servicio de Usuarios
	 */
	public UserController(ComentarioServicioI comentariosServicio, UsuarioServicioImpl usuarioServicio) {
		this.comentariosServicio = comentariosServicio;
		this.usuarioServicio = usuarioServicio;
	}

	private static final String HOME_URL = "auth/user/home";

	/**
	 * Maneja las solicitudes GET a "/user/home".
	 *
	 * @param model          El modelo de Spring MVC
	 * @param authentication Objeto de autenticación de Spring Security
	 * @param page           Número de página actual
	 * @param size           Tamaño de la página
	 * @param usuarioId      ID del usuario a filtrar (opcional)
	 * @param keyword        Palabra clave para búsqueda (opcional)
	 * @param request        HttpServletRequest para obtener la URL de la solicitud
	 * @return La vista de la página de inicio del usuario
	 */
	@GetMapping("/home")
	@PreAuthorize("hasRole('USER')")
	public String user(Model model, Authentication authentication,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "usuarioId", required = false) Long usuarioId,
			@RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request) {

		model.addAttribute("comentario", new Comentario());

		String username = authentication.getName(); // Obtener el nombre de usuario del objeto de autenticación
		model.addAttribute("username", username); // Agregarlo al modelo
		UsuarioDTO usuarioDTO = usuarioServicio.obtenerUsuarioDTO(username);
		model.addAttribute("usuarioDTO", usuarioDTO);

		Slice<Comentario> sliceComentarios;
		if (keyword != null && !keyword.isEmpty()) {
			// Lógica para buscar comentarios por palabra clave
			sliceComentarios = comentariosServicio.buscarComentariosPorPalabraClave(keyword,
					PageRequest.of(page, size));
		} else if (usuarioId != null && usuarioId != 0) {
			// Filtrar comentarios por usuarioId si se proporciona
			sliceComentarios = comentariosServicio.listarPorUsuarioComoSlice(usuarioId, PageRequest.of(page, size));
		} else {
			// Si no se proporciona usuarioId o palabra clave, listar todos los comentarios
			sliceComentarios = comentariosServicio.listarTodosComoSlice(PageRequest.of(page, size));
		}

		int currentPage = page;
		int startPage = Math.max(0, currentPage - 2);
		int totalPages = sliceComentarios.isLast() ? currentPage : currentPage + 2;
		int endPage = Math.min(totalPages, currentPage + 2);

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("comentarios", sliceComentarios.getContent());
		model.addAttribute("requestURI", request.getRequestURI());
		model.addAttribute("hasNext", sliceComentarios.hasNext());
		model.addAttribute("hasPrevious", sliceComentarios.hasPrevious());
		model.addAttribute("currentPage", currentPage);

		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedUserId", usuarioId); // Añade el ID del usuario seleccionado al modelo

		// Añadir la lista de usuarios para el filtrado
		model.addAttribute("usuarios", usuarioServicio.listarTodosLosUsuariosConRolUser());

		return HOME_URL; // Muestra la página específica del usuario (user.html)
	}

	/**
	 * Maneja las solicitudes GET a "/user/agregarComentario".
	 *
	 * @param model El modelo de Spring MVC
	 * @return La vista del formulario de creación de comentarios
	 */
	@GetMapping("/agregarComentario")
	public String mostrarFormularioComentario(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		// Obtener UsuarioDTO en lugar de la entidad Usuario completa
		UsuarioDTO usuarioDTO = usuarioServicio.obtenerUsuarioDTO(username);

		// Crear una nueva instancia de Comentario y añadirla al modelo
		Comentario comentario = new Comentario();
		comentario.setFechaCreacion(LocalDateTime.now());
		model.addAttribute("comentario", comentario);
		model.addAttribute("usuarioDTO", usuarioDTO); // Añadir el UsuarioDTO al modelo si es necesario

		return "/auth/user/formCrearComentario";
	}

	/**
	 * Maneja las solicitudes POST a "/user/agregarComentario".
	 *
	 * @param comentario Comentario a agregar
	 * @param result     Resultado de la validación
	 * @param usuarioId  ID del usuario asociado al comentario
	 * @param model      El modelo de Spring MVC
	 * @return Redirige a la página de inicio del usuario
	 */
	@PostMapping("/agregarComentario")
	public String agregarComentario(@Valid @ModelAttribute("comentario") Comentario comentario, BindingResult result,
			@RequestParam("usuarioId") Long usuarioId, Model model) {
		if (result.hasErrors()) {
			return "/auth/user/formCrearComentario";
		}
		// Busca el usuario por el ID capturado
		Usuario usuario = usuarioServicio.obtenerPorId(usuarioId);
		// Asocia el usuario al comentario
		comentario.setUsuario(usuario);

		comentariosServicio.guardar(comentario);
		return "redirect:/user/home";
	}
}
