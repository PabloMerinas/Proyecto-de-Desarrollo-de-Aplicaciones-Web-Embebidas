package com.example.demo.services.userServices;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.persistence.model.Usuario;

public interface UsuarioServicioI {

	/**
	 * Guarda un usuario
	 * 
	 * @param usuario Usuario a guardar.
	 * @return Usuario guardado
	 */
	public Usuario guardar(Usuario usuario);

	/**
	 * Obtiene un usuario por su ID.
	 * 
	 * @param id Id del usuario a buscar.
	 * @return Usuario encontrado.
	 */
	public Usuario obtenerPorId(Long id);

	/**
	 * Obtiene un usuario por su username.
	 * 
	 * @param username Username del usuario a buscar.
	 * @return Usuario encontrado.
	 */
	public Optional<Usuario> obtenerPorUsername(String username);

	/**
	 * Obtiene un usuario DTO
	 * 
	 * @param username Username del usuario a buscar.
	 * @return UsuarioDTO encontrado.
	 */
	public UsuarioDTO obtenerUsuarioDTO(String username);

	/**
	 * Convierte un usuario a usuarioDTO
	 * 
	 * @param usuario Usuario que se va a convertir.
	 * @return UsuarioDTO convertido.
	 */
	public UsuarioDTO convertirAUsuarioDTO(Usuario usuario);

	/**
	 * Lista de los usuarios que tienen el rol User
	 * 
	 * @return Lista con los usuarios.
	 */
	public List<Usuario> listarTodosLosUsuariosConRolUser();

	/**
	 * Lista de todos los usuarios sin importar su rol
	 * 
	 * @return Lista de todos los usuarios
	 */
	public List<Usuario> listarTodosLosUsuarios();

	/**
	 * Elimina un usuario por su ID.
	 * 
	 * @param id Id del usuario a eliminar.
	 */
	public void eliminarUsuario(Long id);

	/**
	 * Añade un usuario.
	 * 
	 * @param usuario Usuario a añadir.
	 */
	public void añadirUsuario(Usuario usuario);

	/**
	 * Lista de todos los usuarios que tienen el rol de Admin.
	 * 
	 * @return Lista de los usuarios.
	 */
	public List<Usuario> listarTodosLosUsuariosConRolAdmin();
}
