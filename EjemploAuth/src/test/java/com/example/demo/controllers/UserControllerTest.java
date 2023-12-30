package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.persistence.model.Comentario;
import com.example.demo.persistence.model.Usuario;
import com.example.demo.services.comentServices.ComentarioServicioI;
import com.example.demo.services.userServices.UsuarioServicioImpl;

class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	ComentarioServicioI comentariosServicio;

	@Mock
	UsuarioServicioImpl usuarioServicio;

	@Mock
	Model model;

	@SuppressWarnings("deprecation")
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testMostrarFormularioComentario() {
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("testUser");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "testUser", null);

		when(usuarioServicio.obtenerUsuarioDTO(anyString())).thenReturn(usuarioDTO);

		Model model = mock(Model.class); // Crear un mock de Model

		String viewName = userController.mostrarFormularioComentario(model);

		assertEquals("/auth/user/formCrearComentario", viewName);

		verify(model).addAttribute(eq("comentario"), any(Comentario.class)); // Usar eq para el nombre del atributo
		verify(model).addAttribute(eq("usuarioDTO"), eq(usuarioDTO)); // Usar eq para el nombre del atributo y valor
																		// concreto
	}

	@Test
	void testAgregarComentario() {
		Comentario comentario = new Comentario();
		comentario.setContenido("Test comment");
		comentario.setFechaCreacion(LocalDateTime.now());

		BindingResult result = mock(BindingResult.class);

		Usuario usuario = new Usuario();
		usuario.setId(1L);

		when(result.hasErrors()).thenReturn(false);
		when(usuarioServicio.obtenerPorId(anyLong())).thenReturn(usuario);

		String viewName = userController.agregarComentario(comentario, result, 1L, model);

		assertEquals("redirect:/user/home", viewName);

		verify(comentariosServicio).guardar(comentario);
	}
}
