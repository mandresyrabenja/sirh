package mg.softlab.sirh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SirhApplication {

	public static void main(String[] args) {
		SpringApplication.run(SirhApplication.class, args);
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
