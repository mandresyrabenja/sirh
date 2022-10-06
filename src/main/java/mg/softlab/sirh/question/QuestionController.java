package mg.softlab.sirh.question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService questionService;
    private final JobOfferService jobOfferService;

    @GetMapping
    public List<Question> getJobOfferQuestion(@RequestParam Long jobOfferId) {
        try{
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            return questionService.getJobOfferQuestion(jobOffer);
        }catch (IllegalStateException e) {
            return List.of();
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
