package mg.softlab.sirh.jobOffer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.interview.InterviewService;
import mg.softlab.sirh.job.Job;
import mg.softlab.sirh.job.JobService;
import mg.softlab.sirh.jobOffer.candidateResult.CandidateResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/joboffers")
@RequiredArgsConstructor
@Slf4j
public class JobOfferController {
    private final JobOfferService jobOfferService;
    private final JobService jobService;
    private final InterviewService interviewService;

    /**
     * Avoir le resultat des entretiens des candidats d'une poste triés par point obtenu
     * @param jobOfferId ID de l'offre d'emploi
     * @return le resultat des entretiens des candidats d'une poste triés par point obtenu
     */
    @GetMapping("/candidate-result")
    public Page<CandidateResult> getCandidateResult(@RequestParam("offerId") Long jobOfferId,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {
        return interviewService.orderCandidateResultByPoint(jobOfferId, PageRequest.of(page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            JobOffer jobOffer = jobOfferService.findById(id);
            return new ResponseEntity<>(jobOffer, HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/available")
    public Page<JobOffer> getAvailableJobOffers(@RequestParam int page,
                                                @RequestParam int size) {
        return jobOfferService.getAvailableJobOffers(PageRequest.of(page, size));
    }

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


    @PutMapping("/mark-as-done")
    public ResponseEntity<String> markAsDone(@RequestParam Long offerId) {
        try {
            jobOfferService.markAsDone(offerId);
            return ResponseEntity.ok("Offre numero " + offerId + " marqué comme fini avec succès");
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
