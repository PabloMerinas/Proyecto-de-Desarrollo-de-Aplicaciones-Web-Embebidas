package com.example.demo.configurations;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.persistence.model.Comentario;
import com.example.demo.persistence.model.Usuario;
import com.example.demo.persistence.model.enums.RolUsuario;
import com.example.demo.persistence.repositories.ComentarioRepositoryI;
import com.example.demo.persistence.repositories.UsuarioRepositoryI;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

/**
 * Esta clase inicializa datos de ejemplo en la base de datos al iniciar la
 * aplicación.
 */
@Component
public class DataInitializer implements CommandLineRunner {

	private static final String USER2_TEXT = "user2";

	private static final String USER1_TEXT = "user1";

	private UsuarioRepositoryI usuarioRepository;

	private ComentarioRepositoryI comentarioRepository;

	private PasswordEncoder passwordEncoder;

	private Faker faker = new Faker();

	/**
	 * Constructor de DataInitializer.
	 *
	 * @param usuarioRepository    Repositorio de usuarios
	 * @param comentarioRepository Repositorio de comentarios
	 * @param passwordEncoder      Codificador de contraseñas
	 */
	public DataInitializer(UsuarioRepositoryI usuarioRepository, ComentarioRepositoryI comentarioRepository,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.comentarioRepository = comentarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Método que se ejecuta al iniciar la aplicación.
	 *
	 * @param args Argumentos de línea de comandos
	 * @throws Exception Excepción en caso de error
	 */
	@Override
	public void run(String... args) throws Exception {
		// Asegúrate de que los roles existan

		// Crear o buscar el usuario 'user1' y asignarle el rol 'ROLE_USER'
		try {
			crearOBuscarUsuario(USER1_TEXT, USER1_TEXT, RolUsuario.ROLE_USER);
			crearOBuscarUsuario("admin", "admin", RolUsuario.ROLE_ADMIN);
			crearOBuscarUsuario(USER2_TEXT, USER2_TEXT, RolUsuario.ROLE_USER);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Crear comentarios
		crearComentarioUsuario(USER1_TEXT);
		crearComentarioUsuario(USER2_TEXT);
	}

	@Transactional
	private Usuario crearOBuscarUsuario(String username, String password, RolUsuario rol) {
		return usuarioRepository.findByUsername(username).orElseGet(() -> {
			Usuario nuevoUsuario = new Usuario();
			nuevoUsuario.setUsername(username);
			nuevoUsuario.setPassword(passwordEncoder.encode(password));
			nuevoUsuario.getRoles().add(rol);
			return usuarioRepository.save(nuevoUsuario);
		});
	}

	@Transactional
	private void crearComentarioUsuario(String usuario) {
		Usuario user = usuarioRepository.findByUsername(usuario).orElse(null);
		for (int i = 0; i < 5; i++) {
			Comentario comentario = new Comentario();
			try {
				String contenido = faker.lorem().paragraph();
				if (contenido.length() > 255) {
					contenido = contenido.substring(0, 255);
				}
				comentario.setContenido(contenido);
			} catch (Exception e) {
				// Manejo de excepciones
			}
			comentario.setUsuario(user);
			comentario.setFechaCreacion(LocalDateTime.now());
			comentarioRepository.save(comentario);
		}
	}
}
