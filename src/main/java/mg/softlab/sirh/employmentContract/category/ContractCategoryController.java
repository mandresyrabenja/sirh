package mg.softlab.sirh.employmentContract.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlleur HTTP de l'entité EmploymentCategory
 */
@RestController
@RequestMapping("api/v1/contract-categories")
@RequiredArgsConstructor
@Slf4j
public class ContractCategoryController {
    private final ContractCategoryService contractCategoryService;

    @GetMapping
    public List<EmploymentContractCategory> getAllContractCategories() { return contractCategoryService.findAll(); }

}
