package mg.softlab.sirh.bonus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.interview.Interview;
import mg.softlab.sirh.interview.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bonuses")
@RequiredArgsConstructor
@Slf4j
public class BonusController {
    private final BonusService bonusService;
    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<Object> addCandidateBonus(@RequestParam Long interviewId,
                                            @RequestParam String bonusReason,
                                            @RequestParam Short point) {
        try {
            Interview interview = interviewService.findById(interviewId);
            Bonus bonus = new Bonus();
            bonus.setInterview(interview);
            bonus.setPoint(point);
            bonus.setReason(bonusReason);
            bonusService.addCandidateBonus(bonus);
            return ResponseEntity.ok(bonus);
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
