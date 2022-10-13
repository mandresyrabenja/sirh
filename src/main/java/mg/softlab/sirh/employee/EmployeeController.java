package mg.softlab.sirh.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@RequestParam Long personId,
                                   @RequestParam
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hiringDate,
                                   @RequestParam Long jobId,
                                   @RequestParam Double baseSalary,
                                   @RequestParam Long contractCategoryId,
                                   @RequestParam
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam(required = false) String assignment
                                   ){
        return null;
    }
}
