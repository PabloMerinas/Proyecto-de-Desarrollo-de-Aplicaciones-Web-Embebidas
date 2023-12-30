package com.example.demo.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.persistence.model.Comentario;
import com.example.demo.persistence.model.Usuario;
import com.example.demo.services.comentServices.ComentarioServicioI;
import com.example.demo.services.userServices.UsuarioServicioImpl;

@RunWith(SpringRunner.class)
@WithMockUser(roles = "ADMIN") // Simula un usuario con rol "ADMIN"
@WebMvcTest(AdminController.class)
class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UsuarioServicioImpl usuarioService;

	@MockBean
	private ComentarioServicioI comentarioService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	@WithMockUser // Simula la autenticación de un usuario
	public void testHome() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/home")).andExpect(MockMvcResultMatchers.status().isOk()) // Espera
																													// un
																													// código
																													// de
																													// estado
																													// 200
				.andExpect(MockMvcResultMatchers.view().name(AdminController.URL_ADMIN_HOME));
	}

	@Test
	@WithMockUser // Simula la autenticación de un usuario
	void testUsuarios() throws Exception {
		List<Usuario> usuarios = new ArrayList<>();
		Usuario usuario1 = new Usuario();
		Usuario usuario2 = new Usuario();
		usuarios.add(usuario1);
		usuarios.add(usuario2);

		when(usuarioService.listarTodosLosUsuariosConRolUser()).thenReturn(usuarios);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/usuarios"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("auth/admin/home"))
				.andExpect(MockMvcResultMatchers.model().attribute(AdminController.ADMIN_CONTENT_TEXT,
						"fragments/admin-usuarios"))
				.andExpect(MockMvcResultMatchers.model().attribute(AdminController.USUARIOS_TEXT, usuarios));

	}

	@Test
	@WithMockUser // Simula la autenticación de un usuario
	void testComentarios() throws Exception {
		List<Comentario> comentarios = new ArrayList<>();
		Comentario comentario1 = new Comentario();
		Comentario comentario2 = new Comentario();
		comentarios.add(comentario1);
		comentarios.add(comentario2);

		when(comentarioService.obtenerTodosLosComentarios()).thenReturn(comentarios);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/comentarios"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("auth/admin/home"))
				.andExpect(MockMvcResultMatchers.model().attribute(AdminController.ADMIN_CONTENT_TEXT,
						"fragments/admin-comentarios"))
				.andExpect(MockMvcResultMatchers.model().attribute("comentarios", comentarios));

		// Añade más verificaciones según tus necesidades
	}

	@Test
	@WithMockUser // Simula la autenticación de un usuario
	void testEstadisticas() throws Exception {
		List<Usuario> usuarios = new ArrayList<>();
		Usuario usuario1 = new Usuario();
		Usuario usuario2 = new Usuario();
		usuarios.add(usuario1);
		usuarios.add(usuario2);

		List<Comentario> comentarios = new ArrayList<>();
		Comentario comentario1 = new Comentario();
		Comentario comentario2 = new Comentario();
		comentarios.add(comentario1);
		comentarios.add(comentario2);

		when(usuarioService.listarTodosLosUsuarios()).thenReturn(usuarios);
		when(usuarioService.listarTodosLosUsuariosConRolUser()).thenReturn(usuarios);
		when(usuarioService.listarTodosLosUsuariosConRolAdmin()).thenReturn(usuarios);
		when(comentarioService.obtenerTodosLosComentarios()).thenReturn(comentarios);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/estadisticas"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("auth/admin/home"))
				.andExpect(MockMvcResultMatchers.model().attribute(AdminController.ADMIN_CONTENT_TEXT,
						"fragments/admin-estadisticas"))
				.andExpect(MockMvcResultMatchers.model().attribute(AdminController.USUARIOS_TEXT, usuarios))
				.andExpect(MockMvcResultMatchers.model().attribute("usuariosUser", usuarios))
				.andExpect(MockMvcResultMatchers.model().attribute("usuariosAdmin", usuarios))
				.andExpect(MockMvcResultMatchers.model().attribute("comentarios", comentarios));

		// Añade más verificaciones según tus necesidades
	}

	@Test
	@WithMockUser // Simula la autenticación de un usuario
	void testEliminarUsuario() throws Exception {
		Long usuarioId = 1L;

		ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/eliminarUsuario/{id}", usuarioId))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name(AdminController.REDIRECT_USUARIOS));

		verify(usuarioService).eliminarUsuario(usuarioId);
	}

	@Test
	public void testAnadirUsuario() throws Exception {
		String username = "nuevoUsuario";
		String password = "nuevaContraseña";

		Usuario usuario = new Usuario();
		usuario.setUsername(username);
		usuario.setPassword(password);

		when(usuarioService.obtenerPorUsername(username)).thenReturn(Optional.empty());
		when(passwordEncoder.encode(password)).thenReturn("contraseñaEncriptada");

		mockMvc.perform(post("/admin/anadirUsuario").param("username", username).param("password", password))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(AdminController.REDIRECT_USUARIOS));

		verify(usuarioService).añadirUsuario(usuario);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testEditarUsuario() throws Exception {
		Long usuarioId = 1L;
		String username = "nuevoUsuario";
		String password = "nuevaContraseña";

		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		usuario.setUsername(username);
		usuario.setPassword(password);

		when(usuarioService.obtenerPorId(usuarioId)).thenReturn(usuario);
		when(passwordEncoder.encode(password)).thenReturn("contraseñaEncriptada");

		ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.post("/admin/editarUsuario/{id}", usuarioId).param("username", username)
						.param("password", password))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(AdminController.REDIRECT_USUARIOS));

		verify(usuarioService).guardar(usuario);
	}

	@Test
	@WithMockUser // Simula la autenticación de un usuario
	void testEliminarComentario() throws Exception {
		Long comentarioId = 1L;

		ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/eliminarComentario/{id}", comentarioId))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/admin/comentarios"));

		verify(comentarioService).eliminar(comentarioId);
	}
}
