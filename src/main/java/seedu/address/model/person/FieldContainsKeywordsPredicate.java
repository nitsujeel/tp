package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.pet.Pet;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person} matches all provided field search strings.
 */
public class FieldContainsKeywordsPredicate implements Predicate<Person> {
    private final Optional<String> ownerNameKeyword;
    private final Optional<String> phoneKeyword;
    private final Optional<String> emailKeyword;
    private final Optional<String> addressKeyword;
    private final List<String> tagKeywords;
    private final Optional<String> petNameKeyword;
    private final Optional<String> speciesKeyword;
    private final Optional<String> petRemarkKeyword;

    /**
     * Constructs a predicate that matches persons whose provided fields contain the corresponding search strings.
     */
    public FieldContainsKeywordsPredicate(Optional<String> ownerNameKeyword, Optional<String> phoneKeyword,
                                          Optional<String> emailKeyword, Optional<String> addressKeyword,
                                          List<String> tagKeywords) {
        this(ownerNameKeyword, phoneKeyword, emailKeyword, addressKeyword, tagKeywords,
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Constructs a predicate that matches persons whose provided owner fields contain the corresponding search
     * strings and, when pet fields are provided, have at least one pet matching all provided pet search strings.
     */
    public FieldContainsKeywordsPredicate(Optional<String> ownerNameKeyword, Optional<String> phoneKeyword,
                                          Optional<String> emailKeyword, Optional<String> addressKeyword,
                                          List<String> tagKeywords, Optional<String> petNameKeyword,
                                          Optional<String> speciesKeyword, Optional<String> petRemarkKeyword) {
        requireNonNull(ownerNameKeyword);
        requireNonNull(phoneKeyword);
        requireNonNull(emailKeyword);
        requireNonNull(addressKeyword);
        requireNonNull(tagKeywords);
        requireNonNull(petNameKeyword);
        requireNonNull(speciesKeyword);
        requireNonNull(petRemarkKeyword);
        this.ownerNameKeyword = ownerNameKeyword;
        this.phoneKeyword = phoneKeyword;
        this.emailKeyword = emailKeyword;
        this.addressKeyword = addressKeyword;
        this.tagKeywords = List.copyOf(tagKeywords);
        this.petNameKeyword = petNameKeyword;
        this.speciesKeyword = speciesKeyword;
        this.petRemarkKeyword = petRemarkKeyword;
    }

    @Override
    public boolean test(Person person) {
        return matchesField(person.getName().fullName, ownerNameKeyword)
                && matchesField(person.getPhone().value, phoneKeyword)
                && matchesField(person.getEmail().value, emailKeyword)
                && matchesField(person.getAddress().value, addressKeyword)
                && matchesTags(person.getTags())
                && matchesPets(person.getPets());
    }

    private static boolean matchesField(String fieldValue, Optional<String> keyword) {
        return keyword.map(value -> containsIgnoreCase(fieldValue, value)).orElse(true);
    }

    private boolean matchesTags(Iterable<Tag> tags) {
        return tagKeywords.stream()
                .allMatch(tagKeyword -> containsInAnyTag(tags, tagKeyword));
    }

    private static boolean containsInAnyTag(Iterable<Tag> tags, String tagKeyword) {
        for (Tag tag : tags) {
            if (containsIgnoreCase(tag.tagName, tagKeyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesPets(Iterable<Pet> pets) {
        if (!hasPetCriteria()) {
            return true;
        }

        for (Pet pet : pets) {
            if (matchesPet(pet)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPetCriteria() {
        return petNameKeyword.isPresent() || speciesKeyword.isPresent() || petRemarkKeyword.isPresent();
    }

    private boolean matchesPet(Pet pet) {
        return matchesField(pet.getName().value, petNameKeyword)
                && matchesField(pet.getSpecies().value, speciesKeyword)
                && matchesField(pet.getRemark().value, petRemarkKeyword);
    }

    private static boolean containsIgnoreCase(String fieldValue, String keyword) {
        return fieldValue.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FieldContainsKeywordsPredicate)) {
            return false;
        }

        FieldContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (FieldContainsKeywordsPredicate) other;
        return ownerNameKeyword.equals(otherNameContainsKeywordsPredicate.ownerNameKeyword)
                && phoneKeyword.equals(otherNameContainsKeywordsPredicate.phoneKeyword)
                && emailKeyword.equals(otherNameContainsKeywordsPredicate.emailKeyword)
                && addressKeyword.equals(otherNameContainsKeywordsPredicate.addressKeyword)
                && tagKeywords.equals(otherNameContainsKeywordsPredicate.tagKeywords)
                && petNameKeyword.equals(otherNameContainsKeywordsPredicate.petNameKeyword)
                && speciesKeyword.equals(otherNameContainsKeywordsPredicate.speciesKeyword)
                && petRemarkKeyword.equals(otherNameContainsKeywordsPredicate.petRemarkKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ownerNameKeyword", ownerNameKeyword)
                .add("phoneKeyword", phoneKeyword)
                .add("emailKeyword", emailKeyword)
                .add("addressKeyword", addressKeyword)
                .add("tagKeywords", tagKeywords)
                .add("petNameKeyword", petNameKeyword)
                .add("speciesKeyword", speciesKeyword)
                .add("petRemarkKeyword", petRemarkKeyword)
                .toString();
    }
}
