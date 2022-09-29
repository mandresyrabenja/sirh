package mg.softlab.sirh.degree;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.degreeCategory.DegreeCategory;
import mg.softlab.sirh.person.Person;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Degree {
    @Id
    @SequenceGenerator(
            name = "sequence_degree",
            sequenceName = "sequence_degree"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_degree"
    )
    private Long id;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @JsonBackReference("person_degree")
    private Person person;

    @org.springframework.data.annotation.Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference("category_degree")
    private DegreeCategory category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String establishment;

    @Column(nullable = false)
    private Integer year;
}
