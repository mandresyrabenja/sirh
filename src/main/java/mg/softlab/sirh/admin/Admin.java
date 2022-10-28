package mg.softlab.sirh.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data @Builder @ToString
public class Admin {
    @Id
    @SequenceGenerator(
            name = "sequence_admin",
            sequenceName = "sequence_admin",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_admin"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
