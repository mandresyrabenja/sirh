package mg.softlab.sirh.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.Candidate.Candidate;
import mg.softlab.sirh.degree.Degree;
import mg.softlab.sirh.experience.Experience;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    @Id
    @SequenceGenerator(
            name = "sequence_person",
            sequenceName = "sequence_person"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_person"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String firstname;

    // Date Of Birth
    @Column(nullable = false)
    @JsonProperty("dob")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private Long phone;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private Long cin;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "person")
    @JsonManagedReference("person_candidate")
    private Collection<Candidate> candidates;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "person")
    @JsonManagedReference("person_degree")
    private Collection<Degree> degrees;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "person")
    @JsonManagedReference("person_experience")
    private Collection<Experience> experiences;
}
