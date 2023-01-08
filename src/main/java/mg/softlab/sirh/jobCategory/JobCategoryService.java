package mg.softlab.sirh.jobCategory;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;

    public void deleteJobCategory(Long id) {
        JobCategory jobCategory = findById(id);
        jobCategoryRepository.delete(jobCategory);
    }

    @Transactional
    public void updateJobCategory(Long id, JobCategory newJobCategoryData) {
        JobCategory bdJobCategory = findById(id);
        if(newJobCategoryData.getMinSalary() != null && newJobCategoryData.getMinSalary() != 0) {
            bdJobCategory.setMinSalary(newJobCategoryData.getMinSalary());
        }

        if(newJobCategoryData.getMaxSalary() != null && newJobCategoryData.getMaxSalary() != 0) {
            bdJobCategory.setMaxSalary(newJobCategoryData.getMaxSalary());
        }

        if(newJobCategoryData.getName() != null && !"".equals(newJobCategoryData.getName())) {
            bdJobCategory.setName(newJobCategoryData.getName());
        }
    }

    public JobCategory findById(Long id) {
        return jobCategoryRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucune catégorie professionelle n'a " + id + " comme ID")
        );
    }

    public Page<JobCategory> findAll(Pageable pageable) {
        return jobCategoryRepository.findAll(pageable);
    }

    public void createJobCategory(JobCategory jobCategory) {
        jobCategoryRepository.save(jobCategory);
    }

    public Page<JobCategory> searchJobCat(String name, Pageable pageable) {
        return jobCategoryRepository.searchJobCat(name, pageable);
    }
}
