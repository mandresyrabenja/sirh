package mg.softlab.sirh.Candidate;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import mg.softlab.sirh.person.Person;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/candidates")
@RequiredArgsConstructor
@Slf4j
public class CandidateController {
    private final CandidateService candidateService;
    private final JobOfferService jobOfferService;
    
    @PostMapping
    public ResponseEntity<String> createCandidate(@RequestParam Long jobOfferId,
                                                  @RequestParam String name,
                                                  @RequestParam String firstname,
                                                  @RequestParam
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
                                                  @RequestParam String email,
                                                  @RequestParam(required = false) String address,
                                                  @RequestParam(required = false) Long phone) {
        try {
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);

            if("".equals(name)) {
                return new ResponseEntity<>("Le champ nom ne peut pas être une chaîne de caractère vide", HttpStatus.NOT_ACCEPTABLE);
            }
            if("".equals(firstname) ) {
                return new ResponseEntity<>("Le champ nom ne peut pas être une chaîne de caractère vide", HttpStatus.NOT_ACCEPTABLE);
            }
            if("".equals(email)) {
                return new ResponseEntity<>("Le champ email ne peut pas être une chaîne de caractère vide", HttpStatus.NOT_ACCEPTABLE);
            }

            Person person = new Person();
            person.setName(name);
            person.setFirstname(firstname);
            person.setEmail(email);
            person.setDob(dob);
            if(null != address)
                person.setAddress(address);
            if(null != phone)
                person.setPhone(phone);

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
