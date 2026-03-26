# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**PetLog** is a Java desktop application for pet daycare/boarding managers to manage owner contacts and pet records. It is built on top of the SE-EDU AddressBook-Level3 project.

## Build & Development Commands

```bash
# Build and run all tests (default task)
./gradlew clean test

# Run the application
./gradlew run

# Build a fat JAR (outputs to build/libs/addressbook.jar)
./gradlew shadowJar

# Run checkstyle
./gradlew checkstyleMain checkstyleTest

# Run a single test class
./gradlew test --tests "seedu.address.logic.commands.AddOwnerCommandTest"

# Run a single test method
./gradlew test --tests "seedu.address.logic.commands.AddOwnerCommandTest.someMethod"
```

## Architecture

The app uses a strict 4-component architecture. Each component defines its API as an interface and is implemented by a `{Name}Manager` class. Components interact only through interfaces.

```
UI ──► Logic ──► Model
             └──► Storage ──► Model
```

- **`UI`** (`seedu.address.ui`): JavaFX-based. All UI parts extend `UiPart<T>`. Layouts defined in `.fxml` files under `src/main/resources/view/`. The `MainWindow` owns all panels.
- **`Logic`** (`seedu.address.logic`): `AddressBookParser` dispatches command words to individual `XCommandParser` classes, which produce `Command` objects. Commands execute against `Model` and return `CommandResult`.
- **`Model`** (`seedu.address.model`): In-memory state. `ModelManager` wraps `AddressBook` (which holds a `UniquePersonList`) and `UserPrefs`. Exposes `ObservableList<Person>` for UI binding.
- **`Storage`** (`seedu.address.storage`): Jackson-based JSON persistence. `JsonAdaptedPerson` and `JsonAdaptedPet` are the JSON serialization wrappers. Data is saved to `petlog.json` (path configurable in `UserPrefs`).
- **`Commons`** (`seedu.address.commons`): Shared utilities, exceptions, and `Index` (1-based to 0-based wrapper).

## Domain Model

- **`Person`** (owner): `Name`, `Phone`, `Email`, `Address`, `Set<Tag>`, `Set<Pet>` — all immutable fields.
- **`Pet`**: `PetName`, `Species`, `OwnerIndex`, `PetRemark` — immutable.
- `Person.isSamePerson()` uses name-only equality (weaker); `equals()` compares all fields (stronger). Same pattern applies to `Pet.isSamePet()`.

## CLI Syntax Prefixes (defined in `CliSyntax`)

| Prefix | Field         |
|--------|---------------|
| `on/`  | Owner name    |
| `ph/`  | Phone         |
| `em/`  | Email         |
| `ad/`  | Address       |
| `ot/`  | Tag           |
| `oi/`  | Owner index   |
| `pn/`  | Pet name      |
| `ps/`  | Species       |
| `pr/`  | Pet remark    |

## Available Commands

`addowner`, `addpet`, `edit`, `delete`, `clear`, `find`, `list`, `exit`, `help`

## Known In-Progress Areas

- `AddPetCommand.execute()` contains a stub (`// Parser/model wiring for pets is not implemented yet`).
- `JsonAdaptedPerson.toModelType()` passes `Set.of(null)` as the pets argument — pet persistence from JSON is not yet implemented.

## Adding a New Command

1. Create `XCommand extends Command` in `logic/commands/` with a `COMMAND_WORD` constant.
2. Create `XCommandParser implements Parser<XCommand>` in `logic/parser/` if the command takes arguments.
3. Register the new command word in `AddressBookParser.parseCommand()`.
4. Add any new prefix constants to `CliSyntax`.

## Code Style

Enforced by Checkstyle 11 (`config/checkstyle/checkstyle.xml`), following the [SE-EDU intermediate Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html):
- Max line length: **120 characters**
- No star/wildcard imports; import order: static → `java.*` → `org.*` → `com.*`
- No tabs (spaces only)
- Javadoc required on all public classes and public non-getter/setter methods
- Use `// CHECKSTYLE.OFF: RuleName` / `// CHECKSTYLE.ON: RuleName` to suppress specific rules locally
