package mg.softlab.sirh.person;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public void createPerson(Person person) {
        if(personRepository.existsByEmail(person.getEmail()))
            throw new IllegalStateException("L'email " + person.getEmail() + " existe déjà");
        personRepository.save(person);
    }
}
