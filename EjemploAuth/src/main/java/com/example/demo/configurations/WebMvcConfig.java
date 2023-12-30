package com.example.demo.configurations;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * Configura el LocaleResolver para manejar la configuración regional de la
	 * aplicación.
	 *
	 * @return LocaleResolver para resolver la configuración regional
	 */
	@Bean
	LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	/**
	 * Configura el interceptor de cambio de idioma.
	 *
	 * @return LocaleChangeInterceptor para interceptar cambios en el idioma
	 */
	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	/**
	 * Agrega el interceptor de cambio de idioma al registro de interceptores.
	 *
	 * @param registry InterceptorRegistry donde se registra el interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * Configura el origen de mensajes para la internacionalización de la
	 * aplicación.
	 *
	 * @return MessageSource para cargar mensajes internacionalizados
	 */
	@Bean
	MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("language/messages"); // Cambia "language/messages" al nombre de tus archivos de
														// mensajes
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
