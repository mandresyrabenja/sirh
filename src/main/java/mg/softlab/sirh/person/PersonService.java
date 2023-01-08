package mg.softlab.sirh.person;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Person createPerson(Person person) {
        if(personRepository.existsByEmail(person.getEmail()))
            throw new IllegalStateException("L'email " + person.getEmail() + " existe déjà");
        return personRepository.save(person);
    }

    public Person findById(Long personId) {
        return personRepository.findById(personId).orElseThrow(
                () -> new IllegalStateException("Aucune personne n'a " + personId + " comme ID")
        );
    }

    public Page<Person> findAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(
                () -> new IllegalStateException("Aucune personne n'a " + email + " comme email")
        );
    }

    public Page<Person> searchPerson(String name, Pageable pageable) { return personRepository.searchByName(name, pageable); }
}
