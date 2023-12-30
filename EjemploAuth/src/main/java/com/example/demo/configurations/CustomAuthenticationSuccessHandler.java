package com.example.demo.configurations;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.persistence.model.enums.RolUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Esta clase maneja la redirección después de una autenticación exitosa.
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	/**
	 * Maneja la redirección después de una autenticación exitosa.
	 *
	 * @param request        Solicitud HTTP
	 * @param response       Respuesta HTTP
	 * @param authentication Objeto de autenticación
	 * @throws IOException      Excepción de E/S
	 * @throws ServletException Excepción de Servlet
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String targetUrl = determineTargetUrl(authentication);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	/**
	 * Determina la URL de destino después de una autenticación exitosa.
	 *
	 * @param authentication Objeto de autenticación
	 * @return URL de destino
	 */
	protected String determineTargetUrl(Authentication authentication) {
		boolean isAdmin = authentication.getAuthorities()
				.contains(new SimpleGrantedAuthority(RolUsuario.ROLE_ADMIN.toString()));
		boolean isUser = authentication.getAuthorities()
				.contains(new SimpleGrantedAuthority(RolUsuario.ROLE_USER.toString()));

		if (isAdmin) {
			return "/admin/home"; // URL para administradores
		} else if (isUser) {
			return "/user/home"; // URL para usuarios
		} else {
			throw new IllegalStateException();
		}
	}
}
