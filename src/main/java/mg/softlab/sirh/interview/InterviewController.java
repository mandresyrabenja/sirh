package mg.softlab.sirh.interview;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.candidate.CandidateService;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/interviews")
@RequiredArgsConstructor
@Slf4j
public class InterviewController {
    private final InterviewService interviewService;
    private final CandidateService candidateService;
    private final JobOfferService jobOfferService;

     @GetMapping("/calendar")
    public List<Interview> getInterviewCalendar() { return interviewService.findAll(); }

    @GetMapping
    public List<Interview> getJobInterviews(@RequestParam Long offerId) {
        try {
            JobOffer jobOffer = jobOfferService.findById(offerId);
            return interviewService.findJobInterviews(jobOffer);
        } catch (IllegalStateException e) {
            return List.of();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createJobInterview(@RequestParam Long candidateId,
                                                     @RequestParam Long offerId,
                                                     @RequestParam("dateTime")
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime) {
        try {
            Candidate candidate = candidateService.findById(candidateId);
            JobOffer jobOffer = jobOfferService.findById(offerId);
            return ResponseEntity.ok(interviewService.createInterview(candidate, jobOffer, dateTime));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
