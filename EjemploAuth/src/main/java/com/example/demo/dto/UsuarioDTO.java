package com.example.demo.dto;

import java.util.Set;

/**
 * Esta clase representa un objeto de transferencia de datos (DTO) para
 * usuarios.
 */
public class UsuarioDTO {
	private Long id;
	private String username;
	private Set<String> roles;

	/**
	 * Constructor sin argumentos de UsuarioDTO.
	 */
	public UsuarioDTO() {
	}

	/**
	 * Constructor de UsuarioDTO con parámetros.
	 *
	 * @param id       Identificación del usuario
	 * @param username Nombre de usuario
	 * @param roles    Roles asignados al usuario
	 */
	public UsuarioDTO(Long id, String username, Set<String> roles) {
		this.id = id;
		this.username = username;
		this.roles = roles;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the roles
	 */
	public Set<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}
