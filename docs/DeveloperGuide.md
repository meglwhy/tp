---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

## Product scope

**Target user profile**:

* Social service workers managing multiple households.
* Users who need to track household details and engagement sessions efficiently.
* Prefer a structured system for scheduling and managing household interactions.
* Require features to prevent duplicate entries and session conflicts.
* May work with a large number of records and require filtering/search options.

**Value proposition**: 

Em-Social is a social worker management software designed to streamline household management and session scheduling. It allows users to:
* Add, edit, and categorize household details while preventing duplicate entries.
* Schedule engagement sessions with households while ensuring no double bookings.
* Add notes for each session for accurate record-keeping.
* Search and filter households based on tags or specific criteria.

**Out of scope**:

* Advanced case management with intervention plans.
* Analytics, financial, and resource management.
* Automated reminders beyond basic session notifications.
* Legal compliance tracking.
* Mobile app with offline support.

Em-Social focuses on efficient household and session management, with potential future expansions based on user needs.

## User stories

Priorities: High (must have) - `* * *`, Medium (good to have) - `* *`, Low (unlikely to have) - `*`

### Create & Store Household Records [`* * *`]
**User Story:** As a social service worker, I can create a new household record (address, members, etc.) so that I can easily store and reference important household information.

**Conditions of Satisfaction:**
- I can fill in household address, contact details, and member names.
- The record is saved in the system and is visible in a household list.

### Update & Edit Household Information [`* *`]
**User Story:** As a social service worker, I can update an existing household record so that I can keep the information current and accurate.

**Conditions of Satisfaction:**
- I can edit household details at any time.
- The updated information is saved and immediately visible in the system.

### Add Session Notes [`* *`]
**User Story:** As a social service worker, I can add notes to a session so that I can document important details about household visits.

**Conditions of Satisfaction:**
- I can write session notes when creating or updating a session.
- Notes are saved and linked to the session for future reference.

### Search Household Records [`* *`]
**User Story:** As a social service worker, I can search for households by name or address so that I can quickly find the relevant record.

**Conditions of Satisfaction:**
- I can use a search bar or filter to find households by name and/or address.
- Matching results appear immediately in a results list.

### View List of Households [`* * *`]
**User Story:** As a social service worker, I can view a list of all stored households so that I can quickly browse through records.

**Conditions of Satisfaction:**
- I can see all stored households in a structured list.
- Each household entry displays key information like name, address, and contact details.

### Delete Household [`* * *`]
**User Story:** As a social service worker, I can delete a household record so that I can remove outdated or incorrect information.

**Conditions of Satisfaction:**
- I can delete a household from the system.
- The deleted household is removed from the household list.

### Clear All Entries [`* *`]
**User Story:** As a social service worker, I can clear all stored entries so that I can reset the system when necessary.

**Conditions of Satisfaction:**
- I can remove all stored households and sessions.
- The system confirms before clearing all data.

### Exit Program [`* * *`]
**User Story:** As a social service worker, I can exit the program so that I can close the application safely.

**Conditions of Satisfaction:**
- I can terminate the application using an exit command.
- The system safely closes all sessions and saves necessary data.

### Viewing Help [`* * *`]
**User Story:** As a social service worker, I can view a help menu so that I can understand how to use the system.

**Conditions of Satisfaction:**
- The system provides a help command.
- The help menu explains how to use available features.

### Receive Session Notification [`* *`]
**User Story:** As a social service worker, I receive notifications for upcoming sessions so that I am reminded of my appointments.

**Conditions of Satisfaction:**
- The system sends reminders for scheduled sessions.
- Notifications appear in-app or via email.



## Use cases

(For all use cases below, the **System** is the `Em-Social` and the **Actor** is the `Social Service Worker`, unless specified otherwise)

### Use Case: U1. Create Household Record

**Main Success Scenario:**
1. User selects the option to add a household.
2. System prompts for household details.
3. User enters household name, address, and contact information.
4. System validates the input.
5. System saves the household record.
6. System displays a confirmation message.

    Use case ends.

**Extensions:**

4a. Invalid input provided.  
* 4a1. System displays an error message.  
* 4a2. User corrects input and retries.  
Use case resumes from step 4.

6a. System fails to save the household record.  
* 6a1. System displays an error message indicating the failure.  
* 6a2. System prompts the user to retry saving.  
* 6a3. If the issue persists, the system logs the error for troubleshooting.  
Use case resumes from step 3 or ends if saving is unsuccessful.

---

### Use Case: U2. Schedule Engagement Session

**Main Success Scenario:**
1. User selects a household.
2. User schedules a session.
3. System performs (**Prevent Double-Booking (U4)**) to prevent scheduling conflicts.
4. If there is no conflict, system saves the session and updates the calendar.
5. System displays a confirmation message.

    Use case ends.

**Extensions:**

2a. Invalid date/time format.  
* 2a1. System displays an error message.  
* 2a2. User corrects input and retries.  
Use case resumes from step 2.

---

### Use Case: U3. Edit Household Record

**Main Success Scenario:**
1. User selects a household to edit.
2. System displays household details (i.e., name, address, contact, household ID, tags, sessions).
3. User updates the desired detail (name, address, contact, tag).
4. System validates changes.
5. System saves changes and updates the record.
6. System displays a confirmation message.

    Use case ends.

**Extensions:**

4a. Invalid input provided.  
* 4a1. System displays an error message.  
* 4a2. User corrects input and retries.  
Use case resumes from step 4.

---

### Use Case: U4. Prevent Double-Booking

**Main Success Scenario:**
1. User selects a date and time to schedule a session.
2. System checks for existing sessions at the specified time.
3. If no conflicts exist, the system schedules the session.
4. System displays a confirmation message.

    Use case ends.

**Extensions:**

2a. A session already exists at the selected time.  
* 2a1. System displays a conflict warning.  
Use case ends.

---

### Use Case: U5. View Household Session History

**Main Success Scenario:**
1. User selects a household.
2. User chooses to view session history.
3. System retrieves and displays past sessions in chronological order.
4. User reviews session details.

    Use case ends.

**Extensions:**

3a. No past sessions recorded.  
* 3a1. System displays a message indicating no history available.  
Use case ends.

---

### Use Case: U6. Search for