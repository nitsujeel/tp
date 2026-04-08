package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.StringUtil.normalizeForComparison;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.pet.Pet;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person} matches at least one provided search term.
 * Each whitespace-delimited word in each prefixed input is treated as a separate term.
 */
public class FieldContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> ownerNameTerms;
    private final List<String> phoneTerms;
    private final List<String> emailTerms;
    private final List<String> addressTerms;
    private final List<String> tagTerms;
    private final List<String> petNameTerms;
    private final List<String> speciesTerms;
    private final List<String> petRemarkTerms;

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
     * Constructs a predicate that matches persons whose provided fields contain at least one provided search term.
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
        this.ownerNameTerms = toTerms(ownerNameKeyword);
        this.phoneTerms = toTerms(phoneKeyword);
        this.emailTerms = toTerms(emailKeyword);
        this.addressTerms = toTerms(addressKeyword);
        this.tagTerms = tagKeywords.stream()
                .flatMap(tagKeyword -> toTerms(tagKeyword).stream())
                .collect(Collectors.toUnmodifiableList());
        this.petNameTerms = toTerms(petNameKeyword);
        this.speciesTerms = toTerms(speciesKeyword);
        this.petRemarkTerms = toTerms(petRemarkKeyword);
    }

    @Override
    public boolean test(Person person) {
        if (!hasAnyCriteria()) {
            return true;
        }

        return matchesField(person.getName().fullName, ownerNameTerms)
                || matchesField(person.getPhone().value, phoneTerms)
                || matchesField(person.getEmail().value, emailTerms)
                || matchesField(person.getAddress().value, addressTerms)
                || matchesTags(person.getTags())
                || matchesPetNames(person.getPets())
                || matchesPetSpecies(person.getPets())
                || matchesPetRemarks(person.getPets());
    }

    private boolean hasAnyCriteria() {
        return !ownerNameTerms.isEmpty()
                || !phoneTerms.isEmpty()
                || !emailTerms.isEmpty()
                || !addressTerms.isEmpty()
                || !tagTerms.isEmpty()
                || !petNameTerms.isEmpty()
                || !speciesTerms.isEmpty()
                || !petRemarkTerms.isEmpty();
    }

    private static boolean matchesField(String fieldValue, List<String> terms) {
        if (terms.isEmpty()) {
            return false;
        }

        return terms.stream().anyMatch(term -> containsIgnoreCase(fieldValue, term));
    }

    private boolean matchesTags(Iterable<Tag> tags) {
        if (tagTerms.isEmpty()) {
            return false;
        }

        return tagTerms.stream()
                .anyMatch(tagTerm -> containsInAnyTag(tags, tagTerm));
    }

    private static boolean containsInAnyTag(Iterable<Tag> tags, String tagTerm) {
        for (Tag tag : tags) {
            if (containsIgnoreCase(tag.tagName, tagTerm)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesPetNames(Iterable<Pet> pets) {
        if (petNameTerms.isEmpty()) {
            return false;
        }

        for (Pet pet : pets) {
            if (matchesField(pet.getName().value, petNameTerms)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesPetSpecies(Iterable<Pet> pets) {
        if (speciesTerms.isEmpty()) {
            return false;
        }

        for (Pet pet : pets) {
            if (matchesField(pet.getSpecies().value, speciesTerms)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesPetRemarks(Iterable<Pet> pets) {
        if (petRemarkTerms.isEmpty()) {
            return false;
        }

        for (Pet pet : pets) {
            if (matchesField(pet.getRemark().value, petRemarkTerms)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsIgnoreCase(String fieldValue, String keyword) {
        return normalizeForComparison(fieldValue).contains(normalizeForComparison(keyword));
    }

    private static List<String> toTerms(Optional<String> optionalKeyword) {
        requireNonNull(optionalKeyword);
        return optionalKeyword.map(FieldContainsKeywordsPredicate::toTerms).orElse(List.of());
    }

    private static List<String> toTerms(String keyword) {
        requireNonNull(keyword);
        String normalizedKeyword = normalizeWhitespace(keyword);
        if (normalizedKeyword.isEmpty()) {
            return List.of();
        }

        return Arrays.stream(normalizedKeyword.split(" "))
                .filter(term -> !term.isBlank())
                .collect(Collectors.toUnmodifiableList());
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
        return ownerNameTerms.equals(otherNameContainsKeywordsPredicate.ownerNameTerms)
                && phoneTerms.equals(otherNameContainsKeywordsPredicate.phoneTerms)
                && emailTerms.equals(otherNameContainsKeywordsPredicate.emailTerms)
                && addressTerms.equals(otherNameContainsKeywordsPredicate.addressTerms)
                && tagTerms.equals(otherNameContainsKeywordsPredicate.tagTerms)
                && petNameTerms.equals(otherNameContainsKeywordsPredicate.petNameTerms)
                && speciesTerms.equals(otherNameContainsKeywordsPredicate.speciesTerms)
                && petRemarkTerms.equals(otherNameContainsKeywordsPredicate.petRemarkTerms);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ownerNameTerms", ownerNameTerms)
                .add("phoneTerms", phoneTerms)
                .add("emailTerms", emailTerms)
                .add("addressTerms", addressTerms)
                .add("tagTerms", tagTerms)
                .add("petNameTerms", petNameTerms)
                .add("speciesTerms", speciesTerms)
                .add("petRemarkTerms", petRemarkTerms)
                .toString();
    }
}
