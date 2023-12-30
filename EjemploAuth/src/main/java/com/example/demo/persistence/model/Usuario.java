package com.example.demo.persistence.model;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.persistence.model.enums.RolUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * Esta clase representa una entidad de "Usuario" en la base de datos.
 */
@Table(name = "Usuarios")
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@Size(min = 8) // Asumiendo que quieres una contraseña de al menos 8 caracteres
	private String password;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Comentario> comentarios = new HashSet<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Persona person;

	@ElementCollection(targetClass = RolUsuario.class) // Instrucción de Hibernate para que guarde colecciones
														// enumeradas
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "usuario_rol")
	@Column(name = "RolesUsuario")
	private Set<RolUsuario> roles = new HashSet<>();

	// Getters y setters

	public Long getId() {
		return id;
	}

	/**
	 * @return the person
	 */
	public Persona getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Persona person) {
		this.person = person;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RolUsuario> getRoles() {
		return roles;
	}

	public void setRoles(Set<RolUsuario> roles) {
		this.roles = roles;
	}

	public Set<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(Set<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

}