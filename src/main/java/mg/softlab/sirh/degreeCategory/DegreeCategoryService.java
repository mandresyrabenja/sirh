package mg.softlab.sirh.degreeCategory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DegreeCategoryService {
    private final DegreeCategoryRepository degreeCategoryRepository;

    public void createCategory(DegreeCategory degreeCategory) {
        degreeCategoryRepository.save(degreeCategory);
    }

    public List<DegreeCategory> findAllDegreeCategories() {
        return degreeCategoryRepository.findAll();
    }

    public DegreeCategory findById(Long degreeCatId) {
        return degreeCategoryRepository.findById(degreeCatId).orElseThrow(
                () -> new IllegalStateException("Aucune catégorie de dimplôme n'a " + degreeCatId + " comme ID")
        );
    }
}
