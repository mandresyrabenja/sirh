package mg.softlab.sirh.degreeCategory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeCategoryService {
    private final DegreeCategoryRepository degreeCategoryRepository;

    public void createCategory(DegreeCategory degreeCategory) {
        degreeCategoryRepository.save(degreeCategory);
    }

}
