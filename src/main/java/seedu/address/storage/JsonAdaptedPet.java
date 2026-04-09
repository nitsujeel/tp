package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;
import seedu.address.model.session.Session;

/**
 * Jackson-friendly version of {@link Pet}.
 */
public class JsonAdaptedPet {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pet's %s field is missing!";

    private final String name;
    private final String species;
    private final String remark;
    private final List<JsonAdaptedSession> sessions = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPet} with the given pet details.
     */
    @JsonCreator
    public JsonAdaptedPet(@JsonProperty("name") String name, @JsonProperty("species") String species,
            @JsonProperty("remark") String remark, @JsonProperty("sessions") List<JsonAdaptedSession> sessions) {
        this.name = name;
        this.species = species;
        this.remark = remark;
        if (sessions != null) {
            this.sessions.addAll(sessions);
        }
    }

    /**
     * Converts a given {@code Pet} into this class for Jackson use.
     */
    public JsonAdaptedPet(Pet source) {
        name = source.getName().value;
        species = source.getSpecies().value;
        remark = source.getRemark().value;
        sessions.addAll(source.getSessions().stream()
                .map(JsonAdaptedSession::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted pet object into the model's {@code Pet} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted pet.
     */
    public Pet toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, PetName.class.getSimpleName()));
        }
        if (!PetName.isValidName(name)) {
            throw new IllegalValueException(PetName.MESSAGE_CONSTRAINTS);
        }
        final PetName modelName = new PetName(name);

        if (species == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Species.class.getSimpleName()));
        }
        if (!Species.isValidSpecies(species)) {
            throw new IllegalValueException(Species.MESSAGE_CONSTRAINTS);
        }
        final Species modelSpecies = new Species(species);

        if (remark == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, PetRemark.class.getSimpleName()));
        }
        if (!PetRemark.isValidRemark(remark)) {
            throw new IllegalValueException(PetRemark.MESSAGE_CONSTRAINTS);
        }
        final PetRemark modelRemark = new PetRemark(remark);

        Pet pet = new Pet(modelName, modelSpecies, modelRemark);

        for (JsonAdaptedSession adaptedSession : sessions) {
            Session session = adaptedSession.toModelType();
            pet.addSession(session);
        }

        return pet;
    }
}
