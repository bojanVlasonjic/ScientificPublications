package com.sp.ScientificPublications.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sp.ScientificPublications.models.FusekiConnectionProperties;
import com.sp.ScientificPublications.repository.rdf.AuthenticationUtilities;


@Configuration
public class FusekiConfig {
	
	@Bean
	public FusekiConnectionProperties fusekiConnectionProperties() throws IOException {
		return AuthenticationUtilities.loadProperties();
	}
	
}
