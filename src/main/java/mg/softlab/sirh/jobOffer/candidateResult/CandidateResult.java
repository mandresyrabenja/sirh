package mg.softlab.sirh.jobOffer.candidateResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import mg.softlab.sirh.candidate.Candidate;

/**
 * Classe utilisée pour encapsuler les points obtenus par un candidat durant son entretien
 */
@Data
@AllArgsConstructor
@Builder
public class CandidateResult {
    private Candidate candidate;
    private Double responsePoint;
    private Short bonusPoint;

    /**
     * Avoir le total des points obtenus par le candidat
     * @return Total des point obtenus par le candidat
     */
    public Double getTotal() { return responsePoint + bonusPoint; }
}
