package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;
import seedu.address.testutil.PersonBuilder;

public class FieldContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        FieldContainsKeywordsPredicate firstPredicate = new FieldContainsKeywordsPredicate(
                Optional.of("Alice"), Optional.empty(), Optional.empty(), Optional.empty(), List.of("friend"));
        FieldContainsKeywordsPredicate secondPredicate = new FieldContainsKeywordsPredicate(
                Optional.of("Bob"), Optional.empty(), Optional.empty(), Optional.empty(), List.of("friend"));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        FieldContainsKeywordsPredicate firstPredicateCopy = new FieldContainsKeywordsPredicate(
                Optional.of("Alice"), Optional.empty(), Optional.empty(), Optional.empty(), List.of("friend"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate values -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_allProvidedFieldsMatch_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("alice"),
                Optional.of("9435"),
                Optional.of("example.com"),
                Optional.of("jurong"),
                List.of("friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("94351253")
                .withEmail("alice@example.com").withAddress("123 Jurong West").withTags("friends").build()));
    }

    @Test
    public void test_keywordsWithLongWhitespaceMatchNormalizedFields_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("alice    bob"),
                Optional.empty(),
                Optional.empty(),
                Optional.of("jurong    west"),
                List.of("friEnds"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("123 Jurong West")
                .withTags("friends").build()));
    }

    @Test
    public void test_anyProvidedFieldMatches_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("alice"),
                Optional.of("111"),
                Optional.empty(),
                Optional.empty(),
                Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("94351253").build()));
    }

    @Test
    public void test_anyTagSearchStringMatches_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of("friend", "vip"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friends").build()));
    }

    @Test
    public void test_petCriteriaMatchInSinglePet_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.of("buddy"), Optional.of("dog"), Optional.of("friendly"));
        assertTrue(predicate.test(new PersonBuilder().withPets(
                new Pet(
                        new PetName("Buddy"),
                        new Species("Dog"),
                        new PetRemark("Very friendly"))).build()));
    }

    @Test
    public void test_petCriteriaSplitAcrossDifferentPets_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.of("buddy"), Optional.of("cat"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withPets(
                new Pet(
                        new PetName("Buddy"),
                        new Species("Dog"),
                        new PetRemark("Active")),
                new Pet(
                        new PetName("Mittens"),
                        new Species("Cat"),
                        new PetRemark("Calm"))).build()));
    }

    @Test
    public void test_eachWordInPrefixSearchedSeparately_returnsTrue() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("avi jon"), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.of("sam"), Optional.empty(), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withName("Javier Tan")
                .withPets(new Pet(new PetName("Milo"), new Species("Dog"), new PetRemark("Friendly")))
                .build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Tan")
                .withPets(new Pet(new PetName("Sesame"), new Species("Cat"), new PetRemark("Calm")))
                .build()));
    }

    @Test
    public void test_noMatchingTerms_returnsFalse() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("avi jon"), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.of("sam"), Optional.empty(), Optional.empty());

        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob")
                .withPets(new Pet(new PetName("Buddy"), new Species("Dog"), new PetRemark("Friendly")))
                .build()));
    }

    @Test
    public void toStringMethod() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("alice"), Optional.of("9435"), Optional.empty(), Optional.empty(), List.of("friend"));
        String expected = FieldContainsKeywordsPredicate.class.getCanonicalName() + "{ownerNameTerms=[alice], "
                + "phoneTerms=[9435], emailTerms=[], addressTerms=[], tagTerms=[friend], petNameTerms=[], "
                + "speciesTerms=[], petRemarkTerms=[]}";
        assertEquals(expected, predicate.toString());
    }
}
