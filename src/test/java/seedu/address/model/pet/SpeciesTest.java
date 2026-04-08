package seedu.address.model.pet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SpeciesTest {

    private static final String VALID_MAX_LENGTH_SPECIES = "A".repeat(30);
    private static final String INVALID_TOO_LONG_SPECIES = "A".repeat(31);

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Species(null));
    }

    @Test
    public void constructor_invalidSpecies_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Species(""));
    }

    @Test
    public void isValidSpecies() {
        assertThrows(NullPointerException.class, () -> Species.isValidSpecies(null));

        assertFalse(Species.isValidSpecies(""));
        assertFalse(Species.isValidSpecies(" "));
        assertFalse(Species.isValidSpecies(INVALID_TOO_LONG_SPECIES));

        assertTrue(Species.isValidSpecies("C"));
        assertTrue(Species.isValidSpecies("Dog"));
        assertTrue(Species.isValidSpecies("Sea Lion"));
        assertTrue(Species.isValidSpecies("Cat-1"));
        assertTrue(Species.isValidSpecies("Golden_Retriever"));
        assertTrue(Species.isValidSpecies("#@!"));
        assertTrue(Species.isValidSpecies(VALID_MAX_LENGTH_SPECIES));
        assertTrue(Species.isValidSpecies("  Sea   Lion  "));
    }

    @Test
    public void constructor_whitespaceNormalized() {
        Species withExtraWhitespace = new Species("  Sea   Lion ");
        Species normalized = new Species("Sea Lion");

        assertTrue(withExtraWhitespace.equals(normalized));
    }

    @Test
    public void equals() {
        Species species = new Species("Dog");

        assertTrue(species.equals(new Species("Dog")));
        assertTrue(species.equals(species));
        assertFalse(species.equals(null));
        assertFalse(species.equals(5));
        assertFalse(species.equals(new Species("Cat")));
    }
}
