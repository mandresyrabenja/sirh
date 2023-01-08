package mg.softlab.sirh.degreeCategory;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeCategoryService {
    private final DegreeCategoryRepository degreeCategoryRepository;

    public void createCategory(DegreeCategory degreeCategory) {
        degreeCategoryRepository.save(degreeCategory);
    }

    public Page<DegreeCategory> findAllDegreeCategories(Pageable pageable) {
        return degreeCategoryRepository.findAll(pageable);
    }

    public DegreeCategory findById(Long degreeCatId) {
        return degreeCategoryRepository.findById(degreeCatId).orElseThrow(
                () -> new IllegalStateException("Aucune catégorie de dimplôme n'a " + degreeCatId + " comme ID")
        );
    }
}
