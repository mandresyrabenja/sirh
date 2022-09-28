package mg.softlab.sirh.department;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public void deleteDepartment(Long id) {
        Department department = findById(id);
        departmentRepository.delete(department);
    }

    public void createDepartment(Department department) {
        departmentRepository.save(department);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucun département n'a " + id + " comme id")
        );
    }

    @Transactional
    public void updateDepartment(Long id, Department newDepartmentData) {
        Department dbDepartment = findById(id);
        if(null != newDepartmentData.getName() && newDepartmentData.getName() != "") {
            dbDepartment.setName(newDepartmentData.getName());
        }
    }
}
