package mg.softlab.sirh.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Genre des personnes: MALE pour homme et FEMALE pour femme
 *
 */
@RequiredArgsConstructor
@Getter
public enum Gender {
    MALE("M"),
    FEMALE("F");

    private final String gender;
}
