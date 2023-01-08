package mg.softlab.sirh.question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService questionService;
    private final JobOfferService jobOfferService;

    @GetMapping
    public Page<Question> getJobOfferQuestion(@RequestParam Long jobOfferId,
                                              @RequestParam int page,
                                              @RequestParam int size) {
        try{
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            return questionService.getJobOfferQuestion(jobOffer, PageRequest.of(page, size));
        }catch (IllegalStateException e) {
            return Page.empty();
        }
    }

    @PostMapping
    public ResponseEntity<Object> addJobOfferQuestion(@RequestParam Long jobOfferId,
                                                      @RequestParam String question,
                                                      @RequestParam Short maxMark) {
        try{
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            return ResponseEntity.ok(questionService.addJobOfferQuestion(jobOffer, question, maxMark));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
