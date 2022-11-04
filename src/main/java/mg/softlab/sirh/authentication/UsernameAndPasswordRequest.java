package mg.softlab.sirh.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mg.softlab.sirh.util.Sha256;

/**
 * Class pour deserialiser un authentification JSON.<br>
 * <u>Exemple</u>:<br>
 * {<br>
 *   "username": "jean",<br>
 *   "password": "1234"<br>
 * }
 */
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class UsernameAndPasswordRequest {

    private String username;
    private String password;

    /**
     * Hasher en sha256 le mot de passe
     */
    public void hashPassword() {
        this.password = Sha256.hash(this.password);
    }

}
