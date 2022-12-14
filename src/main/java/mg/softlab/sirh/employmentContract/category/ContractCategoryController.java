package mg.softlab.sirh.employmentContract.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(contractCategoryService.findById(id));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public List<EmploymentContractCategory> getAllContractCategories() { return contractCategoryService.findAll(); }

}
