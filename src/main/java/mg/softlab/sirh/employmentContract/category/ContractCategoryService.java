package mg.softlab.sirh.employmentContract.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractCategoryService {
    private final ContractCategoryRepository contractCategoryRepository;

    public Page<EmploymentContractCategory> findAll(Pageable pageable) {
        return contractCategoryRepository.findAll(pageable);
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
