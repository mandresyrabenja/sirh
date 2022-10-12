package mg.softlab.sirh.question.response;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.candidate.CandidateService;
import mg.softlab.sirh.question.Question;
import mg.softlab.sirh.question.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/responses")
@RequiredArgsConstructor
@Slf4j
public class CandidateResponseController {
    private final CandidateResponseService candidateResponseService;
    private final QuestionService questionService;
    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<Object> addCandidateResponse(@RequestParam Long candidateId,
                                                       @RequestParam Long questionId,
                                                       @RequestParam Double mark) {
        try {
            Candidate candidate = candidateService.findById(candidateId);
            Question question = questionService.findById(questionId);
            if(question.getMaxMark() < mark) {
                throw new IllegalStateException(
                        "Note " + mark + " invalide. Le note max pour cet question est " + question.getMaxMark() +".");
            }

            CandidateResponse candidateResponse = new CandidateResponse();
            candidateResponse.setCandidate(candidate);
            candidateResponse.setQuestion(question);
            candidateResponse.setMark(mark);

            return ResponseEntity.ok(candidateResponseService.createCandidateResponse(candidateResponse));
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
