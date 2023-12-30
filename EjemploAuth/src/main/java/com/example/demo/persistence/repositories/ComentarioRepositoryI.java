package com.example.demo.persistence.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.model.Comentario;

/**
 * Repositorio para la entidad Comentario.
 */
@Repository
public interface ComentarioRepositoryI extends JpaRepository<Comentario, Long> {

	/**
	 * Busca todos los comentarios paginados.
	 *
	 * @param pageable La información de paginación.
	 * @return Una página de comentarios.
	 */
	Page<Comentario> findAll(Pageable pageable);

	/**
	 * Busca comentarios por el ID de usuario paginados.
	 *
	 * @param usuarioId El ID del usuario.
	 * @param pageable  La información de paginación.
	 * @return Una página de comentarios del usuario especificado.
	 */
	Slice<Comentario> findByUsuarioId(Long usuarioId, Pageable pageable);

	/**
	 * Busca comentarios que contengan una palabra clave (ignora mayúsculas y
	 * minúsculas) paginados.
	 *
	 * @param keyword  La palabra clave a buscar.
	 * @param pageable La información de paginación.
	 * @return Una página de comentarios que contienen la palabra clave
	 *         especificada.
	 */
	Slice<Comentario> findByContenidoContainingIgnoreCase(String keyword, Pageable pageable);
}
