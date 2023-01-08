package mg.softlab.sirh.job;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public void deleteJob(Long id) {
        Job job = findById(id);
        jobRepository.delete(job);
    }

    @Transactional
    public void updateJob(Long id, Job newJobData) {
        Job dbJob = findById(id);
        if(null != newJobData.getName()) {
            dbJob.setName(newJobData.getName());
        }
        if(null != newJobData.getDepartment()) {
            dbJob.setDepartment(newJobData.getDepartment());
        }
        if(null != newJobData.getJobCategory()) {
            dbJob.setJobCategory(newJobData.getJobCategory());
        }
    }

    public Job findById(Long id) {
        return jobRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucun travail n'a " + id + " comme ID")
        );
    }

    public Page<Job> findAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    public void createJob(Job job) {
        jobRepository.save(job);
    }

    public Page<Job> searchJob(String name, Pageable pageable) { return jobRepository.searchJob(name, pageable); }
}
