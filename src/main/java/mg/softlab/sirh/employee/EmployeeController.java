package mg.softlab.sirh.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.employmentContract.category.ContractCategoryService;
import mg.softlab.sirh.employmentContract.category.EmploymentContractCategory;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.job.JobService;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ContractCategoryService contractCategoryService;
    private final PersonService personService;
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestParam Long personId,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                          LocalDate hiringDate,
                                                  @RequestParam Long jobId,
                                                  @RequestParam(required = false) String cnaps,
                                                  @RequestParam(required = false) String ostie,
                                                  @RequestParam Double baseSalary,
                                                  @RequestParam Long contractCategoryId,
                                                  @RequestParam
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                     LocalDate startDate,
                                                  @RequestParam(required = false)
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                     LocalDate endDate,
                                                  @RequestParam(required = false) String assignment
                                   )
    {
        try {
            // Vérification du validité du contrat de travail suivant son type
            EmploymentContractCategory contractCategory = contractCategoryService.findById(contractCategoryId);
            // Un CDI ne doît pas avoir une date de fin de contrat
            if("CDI".equalsIgnoreCase(contractCategory.getName()) && null != endDate) {
                throw new IllegalStateException("Un CDI ne peut pas avoir de date de fin de contrat");
            }
            // Touts les contrats autres que le CDI doîvent avoir une date de fin de contrat
            if(!"CDI".equalsIgnoreCase(contractCategory.getName()) && null == endDate) {
                throw new IllegalStateException("Le contrat doit être un CDI pour ne pas avoir de date de fin");
            }
            // Un contrat de test a pour durée maximale de 6 mois
            if("Contrat de Test".equalsIgnoreCase(contractCategory.getName())
                    && (Period.between(startDate, endDate).getMonths() > 6) ) {
                throw new IllegalStateException("La durée maximale d'un contrat de test est de 6 mois");
            }

            Person person = personService.findById(personId);
            Job job = jobService.findById(jobId);

            return ResponseEntity.ok(
                    employeeService.createEmployee(
                        person, cnaps, ostie, hiringDate, job, contractCategory, baseSalary,startDate,
                        endDate, assignment
                    )
            );
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> addCnapsAndOstie(@PathVariable("id") Long id,
                                                   @RequestParam(required = false) String cnaps,
                                                   @RequestParam(required = false) String ostie)
    {
        try{
            employeeService.addCnapsAndOstie(id, cnaps, ostie);
            return ResponseEntity.ok("Employé numero " + id + " mis à jour avec succès");
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable("id") Long id) {
        try{
           return ResponseEntity.ok(employeeService.findById(id));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
