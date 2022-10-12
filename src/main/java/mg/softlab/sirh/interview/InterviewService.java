package mg.softlab.sirh.interview;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.bonus.BonusService;
import mg.softlab.sirh.candidate.Candidate;
import mg.softlab.sirh.candidate.CandidateService;
import mg.softlab.sirh.email.EmailService;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.candidateResult.CandidateResult;
import mg.softlab.sirh.person.Person;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final EmailService emailService;
    private final CandidateService candidateService;

    public List<Interview> findAll() { return interviewRepository.findAll(); }

    public Interview createInterview(Candidate candidate, JobOffer jobOffer, LocalDateTime dateTime) {
        Interview interview = new Interview();
        interview.setCandidate(candidate);
        interview.setJobOffer(jobOffer);
        interview.setDateTime(dateTime);

        // Envoie d'email au candidat pour l'informer de son entretien d'empbauche
        Interview interview1 = interviewRepository.save(interview);
        sendCandidateInterviewMail(interview1);

        return interview1;
    }

    /**
     * Envoyer le convocation d'entrien d'embauche d'un candidat par email
     * @param interview Entretien d'embauche du candidat
     */
    private void sendCandidateInterviewMail(Interview interview) {
        String subject = "Convoncation à l'entretien d'embauche du poste %s";
        String emailBody = """
                Bonjour %s %s,

                Votre candidature a retenu toute notre attention et nous serions ravis de vous rencontrer.
                Seriez-vous disponible le %d-%d-%d à %s:%s pour un échange dans nos locaux?
                Nos bureaux se situent au Lot V23/32 Mahatsinjo, Alasora, Antananarivo 103.
                Nous vous prions de bien vouloir confirmer votre présence en adressant une réponse à ce mail de convocation à l’entretien d’embauche.
                Nous vous souhaitons une belle journée.

                Softlab SARLU
                contact@softlab.mg
                +261 34 83 088 71""";
        LocalDateTime interviewDateTime = interview.getDateTime();
        Person person = interview.getCandidate().getPerson();
        emailService.sendSimpleMessage(person.getEmail(),
                String.format(subject, interview.getJobOffer().getTitle()),
                String.format(emailBody,
                        person.getFirstname(),
                        person.getName(),
                        interviewDateTime.getDayOfMonth(),
                        interviewDateTime.getMonthValue(),
                        interviewDateTime.getYear(),
                        (interviewDateTime.getHour() >= 10) ? interviewDateTime.getHour() : "0" + interviewDateTime.getHour(),
                        (interviewDateTime.getMinute() >= 10) ? interviewDateTime.getMinute() : "0" + interviewDateTime.getMinute()
                )
        );
    }

    public List<Interview> findJobInterviews(JobOffer offer) {
        return interviewRepository.findByJobOffer(offer);
    }

    @Transactional
    public void addCandidateRemark(Long interviewId, String remark) {
        Interview interview = findById(interviewId);
        interview.setRemark(remark);
    }

    public Interview findById(Long id) {
        return interviewRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Aucun entretien d'embauche n'a " + id + " comme ID")
        );
    }

    /**
     * Trier les candidats par la somme des points que chacuns d'eux ont obtenus
     * @param jobOfferId ID de l'offre d'emploi
     * @return Resultat des candidats triées par point obtenus
     */
    public List<CandidateResult> orderCandidateResultByPoint(Long jobOfferId) {
        return interviewRepository.orderCandidateResponseByPoint(jobOfferId)
                .stream()
                .map(res -> {
                    BigInteger candidateId = (BigInteger) res[0];
                    Double point = (Double) res[1];
                    Integer bonus = (Integer) res[2];
                    return CandidateResult.builder()
                            .candidate(candidateService.findById(candidateId.longValue()))
                            .responsePoint(point)
                            .bonusPoint(bonus.shortValue())
                            .build();
                    })
                .collect(Collectors.toList());
    }
}
