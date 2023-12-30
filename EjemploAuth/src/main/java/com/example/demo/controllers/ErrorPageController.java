package com.example.demo.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

	/**
	 * Maneja las solicitudes GET a "/error".
	 *
	 * @param request HttpServletRequest para obtener el código de estado del error
	 * @return La vista de error correspondiente según el código de estado
	 */
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error/404"; // Página de error 404
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error/500"; // Página de error 500
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				return "error/403"; // Página de error 403
			}
		}
		return "error/default"; // Página de error por defecto
	}

}
