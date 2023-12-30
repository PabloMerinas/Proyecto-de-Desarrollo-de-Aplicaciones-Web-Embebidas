package com.example.demo.services.comentServices;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.persistence.model.Comentario;
import com.example.demo.persistence.repositories.ComentarioRepositoryI;

@Service
public class ComentarioServicioImpl implements ComentarioServicioI {

	private ComentarioRepositoryI comentarioRepository;

	/**
	 * @param comentarioRepository
	 */
	public ComentarioServicioImpl(ComentarioRepositoryI comentarioRepository) {
		this.comentarioRepository = comentarioRepository;
	}

	@Override
	public Comentario guardar(Comentario comentario) {
		return comentarioRepository.save(comentario);
	}

	@Override
	public void eliminar(Long id) {
		comentarioRepository.deleteById(id);
	}

	@Override
	public Comentario obtenerPorId(Long id) {
		Optional<Comentario> resultado = comentarioRepository.findById(id);
		if (resultado.isPresent()) {
			return resultado.get();
		} else {
			// Manejar el caso en que el comentario no se encuentra.
			// Podrías lanzar una excepción o devolver null.
			return null;
		}
	}

	@Override
	public Slice<Comentario> listarTodosComoSlice(Pageable pageable) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, "fechaCreacion"));
		return comentarioRepository.findAll(pageable);
	}

	@Override
	public Page<Comentario> listarTodos(Pageable pageable) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, "fechaCreacion"));
		return comentarioRepository.findAll(pageable);
	}

	@Override
	public Slice<Comentario> listarPorUsuarioComoSlice(Long usuarioId, Pageable pageable) {
		return comentarioRepository.findByUsuarioId(usuarioId, pageable);
	}

	@Override
	public Slice<Comentario> buscarComentariosPorPalabraClave(String keyword, Pageable pageable) {
		return comentarioRepository.findByContenidoContainingIgnoreCase(keyword, pageable);
	}

	@Override
	public List<Comentario> obtenerTodosLosComentarios() {
		return comentarioRepository.findAll();
	}

}