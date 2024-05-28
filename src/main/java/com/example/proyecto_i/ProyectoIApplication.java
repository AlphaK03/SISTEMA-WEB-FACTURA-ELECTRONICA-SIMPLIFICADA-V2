package com.example.proyecto_i;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@SpringBootApplication
public class ProyectoIApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoIApplication.class, args);
	}

	@Bean("securityFilterChain")
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		var chain = http
				.authorizeHttpRequests(customizer -> customizer
						//Usuario
						.requestMatchers("/api/user/details").permitAll()
						.requestMatchers("/api/user/details").authenticated()
						//Login
						.requestMatchers("/api/login/login").permitAll()
						.requestMatchers("/api/login/logout").permitAll()
						.requestMatchers("/api/login/current-user").permitAll()
						.requestMatchers("/api/login/current-user").authenticated()
						//Registro
						.requestMatchers("/api/registro/registro").permitAll()
						.requestMatchers("/api/registro/registroExitoso").permitAll()
						//Clientes
						.requestMatchers("/api/clientes/crearCliente").permitAll()
						.requestMatchers("/api/clientes/listar").permitAll()
						//Productos
						.requestMatchers("/api/productos/proveedor").permitAll()
						.requestMatchers("/api/productos/agregar").permitAll()
						.requestMatchers("/api/productos/agregar").authenticated()
						.requestMatchers("/api/productos/listar").permitAll()
						//Facturas
						.requestMatchers("/api/facturas/crearFactura").permitAll()
						.requestMatchers("/api/facturas/searchCliente").permitAll()
						.requestMatchers("/api/facturas/listar").permitAll()
						.requestMatchers("/api/facturas/listar-facturas").permitAll()
						.requestMatchers("/api/facturas/proveedorID").permitAll()
						.requestMatchers("/api/facturas/pdf").permitAll()

						//?
						.requestMatchers("/api/productos/proveedor").hasAnyAuthority("ADM", "PRO")
						.requestMatchers("/api/**").authenticated()
						.requestMatchers("/**").permitAll()
				)
				.exceptionHandling(customizer -> customizer
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.csrf().disable()
				.build();
		return chain;
	}



}
