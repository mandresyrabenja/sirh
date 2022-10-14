package mg.softlab.sirh.employee;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.employmentContract.EmploymentContract;
import mg.softlab.sirh.employmentContract.EmploymentContractService;
import mg.softlab.sirh.employmentContract.category.EmploymentContractCategory;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.person.Person;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmploymentContractService contractService;

    public List<Employee> searchEmployee(String name, String jobName, Long departmentId, int page) {
        Specification<Employee> specification = EmployeeSpecifications.hasNameLike(name)
                .and(EmployeeSpecifications.hasJobLike(jobName))
                .and(EmployeeSpecifications.hasDepartment(departmentId));
        return employeeRepository.findAll(specification, PageRequest.of(page, 10)).getContent();
    }

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
                .job(job)
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
                .renew(Short.valueOf("0"))
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

    public List<Employee> getEmployees(int page) {
        return employeeRepository.findAll(PageRequest.of(page, 10)).getContent();
    }
}
