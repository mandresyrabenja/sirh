package mg.softlab.sirh.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity(name = "simple_user")
@AllArgsConstructor @NoArgsConstructor @Data @Builder @ToString
public class User {
    @Id
    @SequenceGenerator(
            name = "sequence_user",
            sequenceName = "sequence_user",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_user"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
