package seedu.address.model.pet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.session.Session;

public class PetTest {

    private static final PetName VALID_NAME = new PetName("Buddy");
    private static final Species VALID_SPECIES = new Species("Dog");
    private static final PetRemark VALID_REMARK = new PetRemark("Friendly");

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Pet(null, VALID_SPECIES, VALID_REMARK));
        assertThrows(NullPointerException.class, () -> new Pet(VALID_NAME, null, VALID_REMARK));
        assertThrows(NullPointerException.class, () -> new Pet(VALID_NAME, VALID_SPECIES, null));
    }

    @Test
    public void isSamePet() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, VALID_REMARK);

        assertTrue(pet.isSamePet(pet));
        assertFalse(pet.isSamePet(null));
        assertTrue(pet.isSamePet(new Pet(new PetName("Buddy"), new Species("Dog"), new PetRemark("Needs medication"))));
        assertTrue(pet.isSamePet(new Pet(new PetName("  buddy  "), new Species(" dog "), new PetRemark("Friendly"))));
        assertFalse(pet.isSamePet(new Pet(new PetName("Bella"), new Species("Dog"), VALID_REMARK)));
        assertFalse(pet.isSamePet(new Pet(new PetName("Buddy"), new Species("Cat"), VALID_REMARK)));
    }

    @Test
    public void updateRemark() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, VALID_REMARK);

        pet.updateRemark("Needs medication");
        assertEquals(new PetRemark("Needs medication"), pet.getRemark());

        pet.updateRemark("");
        assertEquals(new PetRemark(""), pet.getRemark());
    }

    @Test
    public void sessions() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, VALID_REMARK);
        Session session = new Session("2026-04-06 10:00", "2026-04-06 11:00", 25.0);

        assertTrue(pet.getSessions().isEmpty());

        pet.addSession(session);
        assertEquals(List.of(session), pet.getSessions());
        assertThrows(UnsupportedOperationException.class, () -> pet.getSessions().add(session));
        assertThrows(NullPointerException.class, () -> pet.addSession(null));
    }

    @Test
    public void equals() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, VALID_REMARK);
        Pet samePet = new Pet(new PetName("Buddy"), new Species("Dog"), new PetRemark("Friendly"));
        Pet differentRemarkPet = new Pet(new PetName("Buddy"), new Species("Dog"), new PetRemark("Needs medication"));
        Pet differentNamePet = new Pet(new PetName("Bella"), new Species("Dog"), VALID_REMARK);
        Pet differentSpeciesPet = new Pet(new PetName("Buddy"), new Species("Cat"), VALID_REMARK);

        assertTrue(pet.equals(samePet));
        assertEquals(pet.hashCode(), samePet.hashCode());
        assertTrue(pet.equals(pet));
        assertFalse(pet.equals(null));
        assertFalse(pet.equals(5));
        assertFalse(pet.equals(differentRemarkPet));
        assertFalse(pet.equals(differentNamePet));
        assertFalse(pet.equals(differentSpeciesPet));
    }

    @Test
    public void equals_sessionsDiffer_stillEqual() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, VALID_REMARK);
        Pet samePetWithoutSessions = new Pet(new PetName("Buddy"), new Species("Dog"), new PetRemark("Friendly"));

        pet.addSession(new Session("2026-04-06 10:00", "2026-04-06 11:00", 25.0));

        assertTrue(pet.equals(samePetWithoutSessions));
        assertEquals(pet.hashCode(), samePetWithoutSessions.hashCode());
    }

    @Test
    public void toStringMethod() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, VALID_REMARK);
        String expected = "Name: Buddy; Species: Dog; Remark: Friendly";

        assertEquals(expected, pet.toString());
    }

    @Test
    public void toStringMethod_emptyRemark_showsNone() {
        Pet pet = new Pet(VALID_NAME, VALID_SPECIES, new PetRemark(""));
        String expected = "Name: Buddy; Species: Dog; Remark: None";

        assertEquals(expected, pet.toString());
    }
}
