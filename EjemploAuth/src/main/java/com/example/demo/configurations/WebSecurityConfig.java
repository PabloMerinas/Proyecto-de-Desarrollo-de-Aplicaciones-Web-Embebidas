package com.example.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.services.userServices.UsuarioPersonalizadoDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	private UsuarioPersonalizadoDetailsService userDetailsService;

	/**
	 * Constructor de la clase WebSecurityConfig.
	 *
	 * @param userDetailsService Servicio de detalles personalizados del usuario
	 */
	public WebSecurityConfig(UsuarioPersonalizadoDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Configura las reglas de seguridad para las rutas y solicitudes HTTP.
	 *
	 * @param http HttpSecurity utilizado para configurar las reglas de seguridad
	 * @return SecurityFilterChain que define las reglas de seguridad
	 * @throws Exception Si ocurre un error al configurar las reglas de seguridad
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// Configuración de las reglas de autorización para diferentes rutas
		http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/admin/**").hasRole("ADMIN") // Rutas bajo
																											// "/admin/"
																											// requieren
																											// el rol
																											// ADMIN
				.requestMatchers("/user/**").hasRole("USER") // Rutas bajo "/user/" requieren el rol USER
				.requestMatchers("/login", "/", "/home", "/public/**").permitAll() // Permite el acceso a estas rutas
																					// sin autenticación
				.anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación

		).userDetailsService(userDetailsService)
				// Configuración para el proceso de inicio de sesión
				.formLogin(formLogin -> formLogin.loginPage("/login") // URL personalizada de inicio de sesión
						.successHandler(new CustomAuthenticationSuccessHandler()) // Usa el manejador personalizado
						.permitAll())
				// Configuración para el proceso de cierre de sesión
				.logout(logout -> logout.logoutSuccessUrl("/login?logout").invalidateHttpSession(true) // Invalida la
																										// sesión actual
						.clearAuthentication(true) // Limpia la autenticación
						.deleteCookies("JSESSIONID") // Borra la cookie de sesión (opcional)
						.permitAll()

				);

		return http.build();
	}

	/**
	 * Configura el codificador de contraseñas a utilizar en la aplicación.
	 *
	 * @return PasswordEncoder para codificar y verificar contraseñas
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
