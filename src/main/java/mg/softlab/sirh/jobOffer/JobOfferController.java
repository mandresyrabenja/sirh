package mg.softlab.sirh.jobOffer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.job.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/joboffers")
@RequiredArgsConstructor
@Slf4j
public class JobOfferController {
    private final JobOfferService jobOfferService;
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<String> createJobOffer(@RequestParam String title,
                                                 @RequestParam String description,
                                                 @RequestParam Long jobId) {
        try {
            Job job = jobService.findById(jobId);

            JobOffer jobOffer = new JobOffer();
            jobOffer.setTitle(title);
            jobOffer.setDescription(description);
            jobOffer.setJob(job);
            jobOffer.setIsDone(false);

            jobOfferService.createJobOffer(jobOffer);
            return new ResponseEntity<>("Offre de travail crée avec succès", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
