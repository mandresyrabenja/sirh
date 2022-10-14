package mg.softlab.sirh.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.department.Department;
import mg.softlab.sirh.department.DepartmentService;
import mg.softlab.sirh.jobCategory.JobCategory;
import mg.softlab.sirh.jobCategory.JobCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {
    private final JobService jobService;
    private final JobCategoryService jobCategoryService;
    private final DepartmentService departmentService;

    @GetMapping("/search")
    public List<Job> searchJob(@RequestParam String name) { return jobService.searchJob(name); }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        try {
          jobService.deleteJob(id);
          return ResponseEntity.ok("Travail supprimé avec succès");
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) Long categoryId,
                                            @RequestParam(required = false) Long departmentId) {
        try {
            Job job = new Job();
            if (null != name) {
                job.setName(name);
            }
            if (null != departmentId) {
                Department department = departmentService.findById(departmentId);
                job.setDepartment(department);
            }
            if (null != categoryId) {
                JobCategory category = jobCategoryService.findById(categoryId);
                job.setJobCategory(category);
            }
            jobService.updateJob(id, job);
            return ResponseEntity.ok("Travail mis à jour avec succès");
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @GetMapping(path = "{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobService.findById(id));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Job> findAllJobs() {
        return jobService.findAllJobs();
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestParam String name,
                                            @RequestParam Long categoryId,
                                            @RequestParam Long departmentId) {
        if( (null == name) || (null == categoryId) || (null == departmentId)) {
            return new ResponseEntity<>("Veuillez remplir touts les champs", HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            JobCategory jobCategory = jobCategoryService.findById(categoryId);
            Department department = departmentService.findById(departmentId);

            Job job = new Job();
            job.setName(name);
            job.setJobCategory(jobCategory);
            job.setDepartment(department);
            jobService.createJob(job);

            return new ResponseEntity<>("Travail " + name + " crée avec succès", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
