package com.example.demo.services.userServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.persistence.model.Usuario;
import com.example.demo.persistence.model.enums.RolUsuario;
import com.example.demo.persistence.repositories.UsuarioRepositoryI;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServicioImpl implements UsuarioServicioI {

	private UsuarioRepositoryI usuarioRepositorio;

	/**
	 * @param usuarioRepositorio
	 */
	public UsuarioServicioImpl(UsuarioRepositoryI usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	public Usuario guardar(Usuario usuario) {
		return usuarioRepositorio.save(usuario);
	}

	@Override
	public Usuario obtenerPorId(Long id) {
		Optional<Usuario> resultado = usuarioRepositorio.findById(id);
		if (resultado.isPresent()) {
			return resultado.get();
		} else {
			// Manejar el caso en que el comentario no se encuentra.
			// Podrías lanzar una excepción o devolver null.
			return null;
		}
	}

	@Override
	@Transactional
	public Optional<Usuario> obtenerPorUsername(String username) {
		return usuarioRepositorio.findByUsername(username);
	}

	@Override
	@Transactional
	public UsuarioDTO obtenerUsuarioDTO(String username) {
		Optional<Usuario> resultado = obtenerPorUsername(username);
		if (resultado.isPresent()) {
			Usuario usuario = resultado.get();
			return convertirAUsuarioDTO(usuario);
		} else {
			return null;
		}
	}

	@Override
	public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
		Set<String> roles = usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
		return new UsuarioDTO(usuario.getId(), usuario.getUsername(), roles);
	}

	@Override
	public List<Usuario> listarTodosLosUsuariosConRolUser() {
		List<Usuario> userList = new ArrayList<>();
		for (Usuario usuario : usuarioRepositorio.findAll()) {
			if (usuario.getRoles().contains(RolUsuario.ROLE_USER)) {
				userList.add(usuario);
			}
		}
		return userList;
	}

	@Override
	public List<Usuario> listarTodosLosUsuarios() {
		return usuarioRepositorio.findAll();

	}

	@Override
	public void eliminarUsuario(Long id) {
		usuarioRepositorio.deleteById(id);
	}

	@Override
	public void añadirUsuario(Usuario usuario) {
		usuarioRepositorio.save(usuario);
	}

	@Override
	public List<Usuario> listarTodosLosUsuariosConRolAdmin() {
		List<Usuario> userList = new ArrayList<>();
		for (Usuario usuario : usuarioRepositorio.findAll()) {
			// Comprobar si el conjunto de roles del usuario contiene el rol USER
			if (usuario.getRoles().contains(RolUsuario.ROLE_ADMIN)) {
				userList.add(usuario);
			}
		}
		return userList;
	}

}