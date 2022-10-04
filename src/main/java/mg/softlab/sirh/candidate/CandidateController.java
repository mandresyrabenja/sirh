package mg.softlab.sirh.candidate;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/candidates")
@RequiredArgsConstructor
@Slf4j
public class CandidateController {
    private final CandidateService candidateService;
    private final JobOfferService jobOfferService;
    private final PersonService personService;

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(candidateService.findById(id));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Trier les candidats d'une offre d'emploi par dimplôme, expérience ou age
     * @param jobOfferId ID de l'offre d'emploi<br>
     * @param criteria chaîne de caractère :<br>
     *                 <ul>
     *                  <li><b>"degree"</b> pour diplôme</li>
     *                  <li><b>"experience"</b> pour l'expérience</li>
     *                  <li><b>"age"</b> pour le plus jeune au plus âgé</li>
     *                 </ul>
     * @return Liste des candidats de l'offre d'emploi triée par dimplôme, expérience ou âge
     */
    @GetMapping("/sort")
    public List<Candidate> sortCandidate(@RequestParam Long jobOfferId,
                                         @RequestParam String criteria) {
        return candidateService.sortCandidate(jobOfferId, criteria);
    }

    @GetMapping
    public List<Candidate> getCandidatesByJobOffer(@RequestParam Long jobOfferId) {
        try {
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            return candidateService.findCandidateByJobOffer(jobOffer);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return List.of();
        }
    }

    @PostMapping
    public ResponseEntity<String> createCandidate(@RequestParam Long jobOfferId,
                                                  @RequestParam Long personId) {
        try {
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            Person person = personService.findById(personId);

            candidateService.createCandidate(jobOffer, person);
            String msg = "Candidature pour l'offre numero " + jobOffer.getId() + " crée avec succès";
            log.info(msg);
            return ResponseEntity.ok(msg);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        
    }
}
