package mg.softlab.sirh.degreeCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

}
