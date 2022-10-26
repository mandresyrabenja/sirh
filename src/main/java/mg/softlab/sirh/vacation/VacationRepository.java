package mg.softlab.sirh.vacation;

import mg.softlab.sirh.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    Long countByEmployee(Employee employee);

    List<Vacation> findByEmployeeOrderByStartDateDesc(Employee employee);
}
