package com.example.demo.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.persistence.model.Comentario;
import com.example.demo.services.comentServices.ComentarioServicioI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	private ComentarioServicioI comentarioServicio;

	/**
	 * @param comentarioServicio
	 */
	public MainController(ComentarioServicioI comentarioServicio) {
		this.comentarioServicio = comentarioServicio;
	}

	private static final int TAM_PAGINACION = 5;

	/**
	 * Maneja las solicitudes GET a "/".
	 *
	 * @return La vista de la página de inicio (index.html)
	 */
	@GetMapping("/")
	public String index() {
		return "index"; // Muestra la página de inicio (index.html)
	}

	/**
	 * Maneja las solicitudes GET a "/login".
	 *
	 * @param model   El modelo de Spring MVC
	 * @param request HttpServletRequest para obtener la sesión
	 * @return La vista del formulario de inicio de sesión
	 */
	@GetMapping("/login")
	public String showLoginForm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		boolean isLoggedIn = (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null);
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "public/login"; // Nombre de tu plantilla de inicio de sesión
	}

	/**
	 * Maneja las solicitudes GET a "/home".
	 *
	 * @param model   El modelo de Spring MVC
	 * @param page    Número de página actual
	 * @param request HttpServletRequest para obtener la URL de la solicitud
	 * @return La vista de la página de inicio (public/home.html)
	 */
	@GetMapping("/home")
	public String home(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			HttpServletRequest request) {

		Slice<Comentario> sliceComentarios = comentarioServicio
				.listarTodosComoSlice(PageRequest.of(page, TAM_PAGINACION));

		int currentPage = page;
		int startPage = Math.max(0, currentPage - 2);
		int endPage = currentPage + 2; // Asume un rango fijo de páginas alrededor de la página actual

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("comentarios", sliceComentarios.getContent());
		model.addAttribute("requestURI", request.getRequestURI());
		model.addAttribute("hasNext", sliceComentarios.hasNext());
		model.addAttribute("hasPrevious", sliceComentarios.hasPrevious());
		model.addAttribute("currentPage", currentPage);

		return "public/home";
	}
}
