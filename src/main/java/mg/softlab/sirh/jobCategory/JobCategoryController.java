package mg.softlab.sirh.jobCategory;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/jobcategories")
@RequiredArgsConstructor
@Slf4j
public class JobCategoryController {
    private final JobCategoryService jobCategoryService;

    @GetMapping(path = "search")
    public List<JobCategory> searchJobCategory(@RequestParam String name) { return jobCategoryService.searchJobCat(name); }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> deleteJobCategory(@PathVariable Long id) {
        try {
            jobCategoryService.deleteJobCategory(id);
            return new ResponseEntity<>("Catégorie de travail effacé avec succès", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> updateJobCategory(@PathVariable Long id,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) Double minSalary,
                                                    @RequestParam(required = false) Double maxSalary)
    {
        JobCategory jobCategory = new JobCategory();
        if(name != null && name != "")
            jobCategory.setName(name);

        if(minSalary != null && minSalary != 0)
            jobCategory.setMinSalary(minSalary);

        if(maxSalary != null && maxSalary != 0)
            jobCategory.setMaxSalary(maxSalary);

        try {
            jobCategoryService.updateJobCategory(id, jobCategory);
            String msg = "Catégorie de travail numero " + id + " mis à jour avec succès";
            log.info(msg);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(jobCategoryService.findById(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<JobCategory> findAll() {
        return jobCategoryService.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createJobCategory(@RequestBody JobCategory jobCategory) {
        if( (jobCategory.getName() == null) || (jobCategory.getName() == "") )
            return new ResponseEntity<>("Le nom est obligatoire", HttpStatus.NOT_ACCEPTABLE);

        if (jobCategory.getMinSalary() == null)
            return new ResponseEntity<>("Le champ du salaire minimale est obligatoire", HttpStatus.NOT_ACCEPTABLE);

        if (jobCategory.getMaxSalary() == null)
            return new ResponseEntity<>("Le champ du salaire maximale est obligatoire", HttpStatus.NOT_ACCEPTABLE);

        if(jobCategory.getMinSalary() >= jobCategory.getMaxSalary())
            return new ResponseEntity<>("Le salaire maximale doît être supérieur au salaire minimale", HttpStatus.NOT_ACCEPTABLE);

        jobCategoryService.createJobCategory(jobCategory);
        return new ResponseEntity<>("Catégorie proféssionelle crée avec succès ", HttpStatus.NOT_ACCEPTABLE);
    }
}
