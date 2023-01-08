package mg.softlab.sirh.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE lower(d.name) LIKE lower(concat('%', :name, '%') ) ")
    public Page<Department> searchDepartment(@Param("name") String name, Pageable pageable);
}
