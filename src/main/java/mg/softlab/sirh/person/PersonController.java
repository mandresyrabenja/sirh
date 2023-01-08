package mg.softlab.sirh.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static mg.softlab.sirh.person.Gender.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/persons")
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;

    @GetMapping("/search")
    public Page<Person> searchPerson(@RequestParam String name,
                                     @RequestParam int page,
                                     @RequestParam int size) {
        return personService.searchPerson(name, PageRequest.of(page, size));
    }

    @GetMapping
    public Page<Person> getAllPersons(@RequestParam int page,
                                      @RequestParam int size) {
        return personService.findAllPersons(PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<Object> createPerson(@RequestBody Person person) {
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
        if(null == person.getGender()) {
            return new ResponseEntity<>("Le champ genre est obligatoire", HttpStatus.NOT_ACCEPTABLE);
        }
        if( !MALE.getGender().equalsIgnoreCase(person.getGender()) &&
                !FEMALE.getGender().equalsIgnoreCase(person.getGender()) ) {
            return new ResponseEntity<>("Le genre doît être 'M' pour homme et 'F' pour femme", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            person = personService.createPerson(person);
            log.info("Personne numero " + person.getId() + " créée avec succès");
            return ResponseEntity.ok(person);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
