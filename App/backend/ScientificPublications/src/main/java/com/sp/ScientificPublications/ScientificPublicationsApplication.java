package com.sp.ScientificPublications;

import com.sp.ScientificPublications.repository.exist.ExistUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScientificPublicationsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScientificPublicationsApplication.class, args);
	}

	@Autowired
	private ExistUtilityService existUtilityService;

	@Override
	public void run(String... args) throws Exception {
		existUtilityService.getOrCreateCollection("cover-letters");
		existUtilityService.getOrCreateCollection("paper-reviews");
		existUtilityService.getOrCreateCollection("scientific-papers");
	}
}
