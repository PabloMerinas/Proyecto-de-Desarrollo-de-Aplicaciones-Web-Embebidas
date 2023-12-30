package com.example.demo.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.model.Usuario;

/**
 * Repositorio para la entidad Usuario.
 */
@Repository
public interface UsuarioRepositoryI extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su nombre de usuario.
	 *
	 * @param username El nombre de usuario a buscar.
	 * @return Un objeto Optional que contiene el usuario si se encuentra.
	 */
	Optional<Usuario> findByUsername(String username);

	/**
	 * Verifica si un usuario con el nombre de usuario especificado existe.
	 *
	 * @param username El nombre de usuario a verificar.
	 * @return true si el usuario existe, false en caso contrario.
	 */
	boolean existsByUsername(String username);
}
