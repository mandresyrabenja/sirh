package mg.softlab.sirh.person;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public void createPerson(Person person) {
        if(personRepository.existsByEmail(person.getEmail()))
            throw new IllegalStateException("L'email " + person.getEmail() + " existe déjà");
        personRepository.save(person);
    }

    public Person findById(Long personId) {
        return personRepository.findById(personId).orElseThrow(
                () -> new IllegalStateException("Aucune personne n'a " + personId + " comme ID")
        );
    }

    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(
                () -> new IllegalStateException("Aucune personne n'a " + email + " comme email")
        );
    }
}
