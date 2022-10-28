package mg.softlab.sirh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class SirhApplication {

	public static void main(String[] args) {
		SpringApplication.run(SirhApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(
				Arrays.asList(
						"http://localhost:3000"
				)
		);
		corsConfiguration.setAllowedHeaders(
				Arrays.asList(
						"Origin",
						"Access-Control-Allow-Origin",
						"Content-Type",
						"Accept",
						"Authorization",
						"Origin, Accept",
						"X-Requested-With",
						"Access-Control-Request-Method",
						"Access-Control-Request-Headers"
				)
		);
		corsConfiguration.setExposedHeaders(
				Arrays.asList(
						"Origin",
						"Content-Type",
						"Accept",
						"Authorization",
						"Access-Control-Allow-Origin",
						"Access-Control-Allow-Origin",
						"Access-Control-Allow-Credentials"
				)
		);
		corsConfiguration.setAllowedMethods(
				Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
		);
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	/**
	 * Données minimales necéssaires
	 * @param degreeCategoryService Service d'accès au base de données des catégories des diplômes
	 */
	/*@Bean
	public CommandLineRunner run(DegreeCategoryService degreeCategoryService) {
		return args -> {
			List<DegreeCategory> degreeCategories = List.of(
					new DegreeCategory("Certificat/Brevet", 1),
					new DegreeCategory("Bacc+1", 2),
					new DegreeCategory("Bacc+2", 3),
					new DegreeCategory("Licence", 4),
					new DegreeCategory("Bacc+4", 5),
					new DegreeCategory("Master/Ingéniorat", 6),
					new DegreeCategory("Bacc+6", 7),
					new DegreeCategory("Bacc+7", 8),
					new DegreeCategory("Doctorat ou plus", 9)
			);
			degreeCategories.forEach(degreeCategoryService::createCategory);
		};
	}*/

}
