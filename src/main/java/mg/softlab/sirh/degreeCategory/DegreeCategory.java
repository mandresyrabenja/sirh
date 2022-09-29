package mg.softlab.sirh.degreeCategory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.softlab.sirh.degree.Degree;

import javax.persistence.*;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DegreeCategory {
    @Id
    @SequenceGenerator(
            name = "sequence_degree_category",
            sequenceName = "sequence_degree_category"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_degree_category"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @org.springframework.data.annotation.Transient
    @OneToMany(mappedBy = "category")
    @JsonManagedReference("category_degree")
    private Collection<Degree> degrees;
}
