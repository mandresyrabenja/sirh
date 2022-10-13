package mg.softlab.sirh.employmentContract;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.employmentContract.category.ContractCategoryService;
import mg.softlab.sirh.job.JobService;
import mg.softlab.sirh.person.PersonService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmploymentContractService {
    private final EmploymentContractRepository contractRepository;
    private final PersonService personService;
    private final JobService jobService;
    private final ContractCategoryService contractCategoryService;
}
