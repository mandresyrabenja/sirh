package mg.softlab.sirh.department;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentService;

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        try{
            departmentService.deleteDepartment(id);
            String msg = "Departement " + id + " effacé avec succès";
            log.info(msg);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable Long id,
                                                   @RequestParam(required = false) String name) {
        try{
            Department department = new Department();
            department.setName(name);
            departmentService.updateDepartment(id, department);
            String msg = "Departement " + id + " mis à jour avec succès";
            log.info(msg);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody Department department) {
        try {
            departmentService.createDepartment(department);
            log.info("Département " + department.getName() + " crée avec succès");
            return ResponseEntity.ok("Département " + department.getName() + " crée avec succès");
        } catch (Exception e) {
            log.warn("Echec de création du département " + department.getName());
            return ResponseEntity.badRequest().body("Echec de création du département " + department.getName());
        }
    }

    @GetMapping
    public List<Department> findAllDepartment() {
        return departmentService.findAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            Department department = departmentService.findById(id);
            return new ResponseEntity<>(department, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
