package edu.mx.utdelacosta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${empleosapp.ruta.imagenes}")
	private String rutaImagenes;
	
	@Value("${empleosapp.ruta.cv}")
	private String rutaCv;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//registry.addResourceHandler("/logos/**").addResourceLocations("file:/empleos/img-vacantes/"); // Linux
		registry.addResourceHandler("/logos/**").addResourceLocations("file:"+rutaImagenes); // Windows
		

		// Configuración de los recursos estáticos (archivos de los CV)
		//registry.addResourceHandler("/cv/**").addResourceLocations("file:c:/empleos/files-cv/"); // Windows
		//registry.addResourceHandler("/cv/**").addResourceLocations("file:/empleos/files-cv/");
		registry.addResourceHandler("/cv/**").addResourceLocations("file:"+rutaCv); // Linux
	}
}