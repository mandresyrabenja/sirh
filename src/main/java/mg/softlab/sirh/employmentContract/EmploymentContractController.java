package mg.softlab.sirh.employmentContract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.employmentContract.category.EmploymentContractCategory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;

@RestController
@RequestMapping("api/v1/contracts")
@RequiredArgsConstructor
@Slf4j
public class EmploymentContractController {
    private final EmploymentContractService contractService;

    @PutMapping(path = "{id}")
    public ResponseEntity<String> renewContract(@PathVariable("id") Long contractId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate startDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate endDate,
                                                @RequestParam(required = false) String assignment,
                                                @RequestParam(required = false) Double baseSalary)
    {
        try {
            EmploymentContract contract = contractService.findById(contractId);
            EmploymentContractCategory contractCategory = contract.getCategory();

            // A CDI contract should cannot be renewed
            if("CDI".equalsIgnoreCase(contractCategory.getName())) {
                throw new IllegalStateException("Un CDI ne peut pas être renouvelé.");
            }
            // A CDD period should not be more than 2 years
            if("CDD".equalsIgnoreCase(contractCategory.getName())
                    && (Period.between(startDate, endDate).getYears() >= 2) ) {
                throw new IllegalStateException("Un CDD ne peut pas excéder 2 ans");
            }
            // startDate should be before endDate
            if(Period.between(startDate, endDate).isNegative()) {
                throw new IllegalStateException("La date de debut doît être avant la date de fin");
            }
            if("Contrat de Test".equalsIgnoreCase(contractCategory.getName()) ) {
                // An employee test contract should not be renewed more than 1 time
                if (contract.getRenew() >= 1) {
                    throw new IllegalStateException("Un contrat de test ne peut être renouvelé qu'une seule fois");
                }
                // An employee total test contracts' period should be more than 6 month
                int oldContractMonths = Period.between(contract.getStartDate(), contract.getEndDate()).getMonths();
                int newContractMonths = Period.between(startDate, endDate).getMonths();
                if( (oldContractMonths + newContractMonths) >= 6) {
                    throw new IllegalStateException("Le cumul des contracts de test d'un employé ne peut pas excéder 6mois");
                }
            }

            contractService.renewContract(contractId, startDate, endDate, assignment, baseSalary);
            String msg = "Contrat de travail numero " + contractId + " renouvelé avec succès";
            log.info(msg);
            return ResponseEntity.ok(msg);
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
