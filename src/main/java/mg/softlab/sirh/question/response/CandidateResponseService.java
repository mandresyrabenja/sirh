package mg.softlab.sirh.question.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.candidate.Candidate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CandidateResponseService {
    private final CandidateResponseRepository candidateResponseRepository;

    public CandidateResponse createCandidateResponse(CandidateResponse cancandidateResponse) {
        return candidateResponseRepository.save(cancandidateResponse);
    }
}
