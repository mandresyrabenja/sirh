package mg.softlab.sirh.employmentContract.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContractCategoryService {
    private final ContractCategoryRepository contractCategoryRepository;

    public List<EmploymentContractCategory> findAll() {
        return contractCategoryRepository.findAll();
    }

    public EmploymentContractCategory findById(Long id) {
        return contractCategoryRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Aucune catégorie de contrat de travail n'a " + id + " comme ID"));
    }

    public EmploymentContractCategory createContractCategory(String name) {
        EmploymentContractCategory category = EmploymentContractCategory.builder()
                .name(name)
                .build();
        return contractCategoryRepository.save(category);
    }
}
