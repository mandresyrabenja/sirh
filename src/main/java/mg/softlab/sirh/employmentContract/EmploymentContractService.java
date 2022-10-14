package mg.softlab.sirh.employmentContract;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class EmploymentContractService {
    private final EmploymentContractRepository contractRepository;

    public EmploymentContract createEmploymentContract(EmploymentContract contract) {
        return contractRepository.save(contract);
    }

    public EmploymentContract findById(Long contractId) {
        return contractRepository.findById(contractId).orElseThrow(() ->
                new IllegalStateException("Aucun contrat de travail n' a " + contractId + " comme ID") );
    }

    @Transactional
    public EmploymentContract renewContract(Long contractId, LocalDate startDate, LocalDate endDate, String assignment, Double baseSalary) {
        EmploymentContract contract = findById(contractId);
        if(null != startDate) {
            contract.setStartDate(startDate);
        }
        if(null != endDate) {
            contract.setEndDate(endDate);
        }
        if(null != assignment) {
            contract.setAssignment(assignment);
        }
        if(null != baseSalary) {
            contract.setBaseSalary(baseSalary);
        }
        contract.setRenew((short) (contract.getRenew() + 1));
        return contract;
    }
}
