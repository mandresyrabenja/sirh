package mg.softlab.sirh.vacation;

import mg.softlab.sirh.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    Long countByEmployee(Employee employee);

    Page<Vacation> findByEmployeeOrderByStartDateDesc(Employee employee, Pageable pageable);
}
