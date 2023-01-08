package mg.softlab.sirh.vacation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.employee.Employee;
import mg.softlab.sirh.employee.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/vacations")
@RequiredArgsConstructor
@Slf4j
public class VacationController {
    private final VacationService vacationService;
    private final EmployeeService employeeService;

    /**
     * Avoir le nombre des jours de congé restant d'un employé
     * @param employeeId ID de l'employé
     * @return le nombre des jours de congé restant de l'employé
     */
    @GetMapping("/rest")
    public ResponseEntity<Object> getEmployeeRestOfDayOff(@RequestParam Long employeeId) {
        try {
            Employee employee = employeeService.findById(employeeId);
            return ResponseEntity.ok(vacationService.getRestDayOff(employee));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public Page<Vacation> getEmployeeVacations(@RequestParam Long employeeId,
                                               @RequestParam int page,
                                               @RequestParam int size) {
        try {
            Employee employee = employeeService.findById(employeeId);
            return vacationService.getEmployeeVacations(employee, PageRequest.of(page, size));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return Page.empty();
        }
    }

    @PostMapping
    public ResponseEntity<Object> addEmployeeVacation(@RequestParam Long employeeId,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        LocalDate startDate,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        LocalDate endDate,
                                                      @RequestParam String reason)
    {
        try {
            Employee employee = employeeService.findById(employeeId);
            Vacation vacation = Vacation.builder()
                    .employee(employee)
                    .startDate(startDate)
                    .endDate(endDate)
                    .reason(reason)
                    .build();
            return ResponseEntity.ok(vacationService.addEmployeeVacation(vacation));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
