package mg.softlab.sirh.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.employmentContract.category.ContractCategoryService;
import mg.softlab.sirh.employmentContract.category.EmploymentContractCategory;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.job.JobService;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import mg.softlab.sirh.util.file.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
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
    @Value("${upload.location}")
    private String FILE_DIRECTORY;

    /**
     * Avoir le photo d'un employé
     * @param id ID de l'employé
     * @return le photo de l'employé
     */
    @GetMapping(path = "/{id}/photo", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getEmployeePhoto(@PathVariable("id") Long id) {
        try {
            return Files.readAllBytes(
                    Paths.get(FILE_DIRECTORY, "employee",  id.toString() + ".png")
            );
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }


    /**
     * Avoir le path du photo d'un employé
     * @param id ID de l'employé
     * @return le path photo de l'employé
     */
    @GetMapping(path = "/{id}/photo-path")
    public String getEmployeePhotoPath(@PathVariable("id") Long id) {
        try {
            return Paths.get(FILE_DIRECTORY, "employee",  id.toString() + ".png").toAbsolutePath().toString();
        } catch (InvalidPathException | SecurityException e) {
            log.warn(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Ajouter le photo d'un employé
     * @param employeeId ID de l'employé
     * @param photo Photo de l'employé
     * @return Reponse HTTP indiquant le succès ou l'echec de l'opération
     */
    @PutMapping(path = "{id}/photo")
    public ResponseEntity<String> addEmployeePhoto(@PathVariable("id") Long employeeId,
                                                             @RequestParam("photo") MultipartFile photo)
    {
        try {
            if(!employeeService.existsById(employeeId)) {
                throw new IllegalStateException("Aucun employé n'a " + employeeId + " comme ID");
            }

            File.saveFile(photo, FILE_DIRECTORY +"/employee", employeeId + ".png");

            String msg = "Photo de l'employé numero " + employeeId + " ajoutée avec succès";
            log.info(msg);
            return ResponseEntity.ok(msg);
        } catch (IllegalStateException | IOException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/search")
    public Page<Employee> searchEmployee(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String jobName,
                                         @RequestParam(required = false) Long departmentId,
                                         @RequestParam int page,
                                         @RequestParam int size)
    {
        return employeeService.searchEmployee(name, jobName, departmentId, PageRequest.of(page, size));
    }

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
            // startDate should be before endDate
            if(!"CDI".equalsIgnoreCase(contractCategory.getName())
                    && Period.between(startDate, endDate).isNegative()) {
                throw new IllegalStateException("La date de debut doît être avant la date de fin");
            }
            // Un CDD ne peut pas excéder deux ans
            if("CDD".equalsIgnoreCase(contractCategory.getName())
                    && (Period.between(startDate, endDate).getYears() >= 2)) {
                throw new IllegalStateException("Un CDD ne peut pas excéder 2 ans ou plus");
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

    @GetMapping
    public Page<Employee> getEmployees(@RequestParam int page,
                                       @RequestParam int size)
    {
        return employeeService.getEmployees(PageRequest.of(page, size));
    }
}
