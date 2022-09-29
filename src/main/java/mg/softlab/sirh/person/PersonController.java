package mg.softlab.sirh.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.jobOffer.JobOffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("api/v1/persons")
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;

    @PostMapping
    public ResponseEntity<String> createPerson(@RequestBody Person person) {
        if(null == person.getName() || "".equals(person.getName())) {
            return new ResponseEntity<>("Le champ nom est obligatoire", HttpStatus.NOT_ACCEPTABLE);
        }
        if(null == person.getFirstname() || "".equals(person.getFirstname())) {
            return new ResponseEntity<>("Le champ nom est obligatoire", HttpStatus.NOT_ACCEPTABLE);
        }
        if(null == person.getEmail() || "".equals(person.getEmail())) {
            return new ResponseEntity<>("Le champ email est obligatoire", HttpStatus.NOT_ACCEPTABLE);
        }
        if(null == person.getDob()) {
            return new ResponseEntity<>("Le champ date de naissance est obligatoire", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            personService.createPerson(person);
            return new ResponseEntity<>("Personne crée avec succès", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
