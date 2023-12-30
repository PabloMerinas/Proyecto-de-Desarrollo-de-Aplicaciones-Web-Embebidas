package com.example.demo.services.comentServices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.demo.persistence.model.Comentario;

public interface ComentarioServicioI {

	/**
	 * Recupera una página de comentarios.
	 * 
	 * @param pageable Parámetro que contiene la información de paginación y
	 *                 ordenación.
	 * @return Una página de comentarios según los parámetros de paginación.
	 */
	Page<Comentario> listarTodos(Pageable pageable);

	/**
	 * Obtiene todos los comentarios.
	 * 
	 * @return Lista con todos los comentarios.
	 */
	List<Comentario> obtenerTodosLosComentarios();

	/**
	 * Guarda un comentario en la base de datos.
	 * 
	 * @param comentario El comentario a guardar.
	 * @return El comentario guardado con su ID generado.
	 */
	Comentario guardar(Comentario comentario);

	/**
	 * Elimina un comentario de la base de datos.
	 * 
	 * @param id El ID del comentario a eliminar.
	 */
	void eliminar(Long id);

	/**
	 * Busca un comentario por su ID.
	 * 
	 * @param id El ID del comentario a buscar.
	 * @return El comentario encontrado o null si no se encuentra.
	 */
	Comentario obtenerPorId(Long id);
//cuando solo necesites saber si hay más elementos después del conjunto actual y quieras evitar la consulta de conteo adicional. 

	/**
	 * Recupera una porción (slice) de comentarios.
	 * 
	 * Este método es útil cuando se necesita paginación pero no se requiere
	 * información sobre el número total de páginas o elementos. Ideal para
	 * situaciones de carga más datos o scroll infinito.
	 * 
	 * @param pageable Parámetro que contiene la información de paginación.
	 * @return Una porción de comentarios según los parámetros de paginación.
	 */
	Slice<Comentario> listarTodosComoSlice(Pageable pageable);

	/**
	 * Recupera los comentarios del usuario que se le pasa.
	 * 
	 * @param usuarioId Id del usuario.
	 * @param pageable  Parametro que contiene la informacion de paginacion.
	 * @return Parte de comentarios.
	 */
	Slice<Comentario> listarPorUsuarioComoSlice(Long usuarioId, Pageable pageable);

	/**
	 * Busca comentarios que contengan la palabra clave en su contenido.
	 * 
	 * @param keyword  La palabra clave para buscar en el contenido del comentario.
	 * @param pageable Objeto Pageable para la paginación de resultados.
	 * @return Una porción (Slice) de comentarios que coincidan con la palabra
	 *         clave.
	 */
	Slice<Comentario> buscarComentariosPorPalabraClave(String keyword, Pageable pageable);

}
