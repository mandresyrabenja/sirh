package mg.softlab.sirh.employmentContract.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContractCategoryService {
    private final ContractCategoryRepository contractCategoryRepository;

    public List<EmploymentContractCategory> findAll() {
        return contractCategoryRepository.findAll();
    }
}
