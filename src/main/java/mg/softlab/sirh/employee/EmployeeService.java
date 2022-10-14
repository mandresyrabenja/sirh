package mg.softlab.sirh.employee;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.employmentContract.EmploymentContract;
import mg.softlab.sirh.employmentContract.EmploymentContractService;
import mg.softlab.sirh.employmentContract.category.EmploymentContractCategory;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.person.Person;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmploymentContractService contractService;

    public Employee createEmployee(Employee employee) { return employeeRepository.save(employee); }

    @Transactional
    public Employee createEmployee(Person person, String cnaps, String ostie, LocalDate hiringDate, Job job,
                                   EmploymentContractCategory contractCategory, Double baseSalary,
                                   LocalDate startDate, LocalDate endDate, String assignment
                                   )
    {
        //Insertion dans le table employee
        Employee employee = Employee.builder()
                .person(person)
                .cnaps(cnaps)
                .ostie(ostie)
                .hiringDate(hiringDate)
                .vacationOrigin(hiringDate)
                .build();
        createEmployee(employee);

        // Insertion du contrat de travail dans le base des données
        EmploymentContract contract = EmploymentContract.builder()
                .employee(employee)
                .job(job)
                .baseSalary(baseSalary)
                .category(contractCategory)
                .startDate(startDate)
                .endDate(endDate)
                .assignment(assignment)
                .build();
        contractService.createEmploymentContract(contract);

        return employee;
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Aucun employé n'a " + id + " comme ID"));
    }

    @Transactional
    public void addCnapsAndOstie(Long employeeId, String cnaps, String ostie) {
        Employee employee = findById(employeeId);
        if(null != cnaps) {
            employee.setCnaps(cnaps);
        }
        if (null != ostie) {
            employee.setOstie(ostie);
        }
    }
}
