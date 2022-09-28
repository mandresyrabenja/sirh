package mg.softlab.sirh.jobCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobCategory {
    @Id
    @SequenceGenerator(
            name = "sequence_jobcategory",
            sequenceName = "sequence_jobcategory"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_jobcategory"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double minSalary;
}
