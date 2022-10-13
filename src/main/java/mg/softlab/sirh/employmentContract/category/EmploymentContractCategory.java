package mg.softlab.sirh.employmentContract.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentContractCategory {
    @Id
    @SequenceGenerator(
            name = "sequence_employment_contract_cat",
            sequenceName = "sequence_employment_contract_cat"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_employment_contract_cat"
    )
    private Long id;

    @Column(nullable = false)
    private String name;
}
