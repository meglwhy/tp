# Em-Social Developer Guide

--------------------------------------------------------------------------------------------------------------------
## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------
## Product Scope

### Target User Profile

- **Efficient Management:**
  Has a need to manage a significant number of household records and engagement sessions efficiently.

- **Monitoring Household Conditions:**
  Has a need to take note of household conditions when there are areas of concern.

- **Desktop Preference:**
  Prefers desktop applications over web or mobile solutions for stability and performance.

- **CLI Enthusiast:**
  Prefers typing over mouse interactions, favoring a Command Line Interface (CLI) for speed.

- **Comfort with CLI:**
  Is reasonably comfortable using CLI applications, with minimal reliance on Graphical User Interfaces (GUI).

### Value Proposition

**Em-Social** enables social service workers to manage household records and schedule sessions faster than traditional mouse/GUI-driven applications by leveraging an efficient CLI interface and structured data handling. This ensures that users can quickly search, update, and schedule without unnecessary navigation overhead, improving productivity and accuracy. By helping social workers to stay organized, they can focus on supporting households instead of managing logistics.

--------------------------------------------------------------------------------------------------------------------
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

--------------------------------------------------------------------------------------------------------------------
## Use cases

(For all use cases below, the **System** is the `Em-Social` and the **Actor** is the `Social Service Worker`, unless specified otherwise)

### Use Case: U1. Create Household Record

**Main Success Scenario:**
1. User adds a household and enters household name, address, and contact information.
2. System prompts for household details.
3. System validates the input.
4. System saves the household record.
5. System displays a confirmation message.

    Use case ends.

**Extensions:**

3a. Invalid input provided.
* 3a1. System displays an error message.
Use case resumes from step 1.

5a. System fails to save the household record.
* 5a1. System displays an error message indicating the failure.
* 5a2. System prompts the user to retry saving.
* 5a3. If the issue persists, the system logs the error for troubleshooting.
Use case resumes from step 3 or ends if saving is unsuccessful.

---

### Use Case: U2. Schedule Engagement Session

**Main Success Scenario:**
1. User schedules an engagement session with a specified household.
2. System performs (**Prevent Double-Booking (U4)**) to prevent scheduling conflicts.
3. If there is no conflict, system saves the session and updates the calendar.
4. System displays a confirmation message.

    Use case ends.

**Extensions:**

1a. Invalid date/time format.
* 1a1. System displays an error message.
Use case resumes from step 1.

---

### Use Case: U3. Edit Household Record

**Main Success Scenario:**
1. User specifies a household to edit and the detail to change.
2. System validates changes.
3. System saves changes and updates the record.
4. System displays a confirmation message.

    Use case ends.

**Extensions:**

2a. Invalid input provided.
* 2a1. System displays an error message.
Use case resumes from step 1.

---

### Use Case: U4. Prevent Double-Booking

**Main Success Scenario:**
1. User selects a date and time to schedule a session.
2. System checks for existing sessions at the specified time.
3. System displays a confirmation message.

    Use case ends.

**Extensions:**

2a. A session already exists at the selected time.
* 2a1. System displays a conflict warning and session is not scheduled.

    Use case ends.

---

### Use Case: U5. View Household Session History

**Main Success Scenario:**
1. User views session history for a specific household.
2. System retrieves and displays past sessions in chronological order.
3. User reviews session details.

    Use case ends.

**Extensions:**

2a. No past sessions recorded.
* 2a1. System displays a message indicating no history available.

    Use case ends.

---

### Use Case: U6. Search for Household

**Main Success Scenario:**
1. User searches a keyword in the search bar.
2. System retrieves matching household records.
3. System displays a list of results.
4. User selects a household for further action.

    Use case ends.

**Extensions:**

2a. No matching records found.
* 2a1. System displays an error message.

    Use case ends.

---

### Use Case: U7. Delete Household

**Main Success Scenario:**
1. User specifies a household to delete.
2. System checks if the household exists.
3. System prompts the user for confirmation.
4. User confirms the deletion.
5. System removes the household record and its sessions from the list.
6. System displays a confirmation message.

    Use case ends.

**Extensions:**

2a. Household ID does not exist.
* 2a1. System displays an error message.
Use case ends.

3a. User cancels deletion.
* 3a1. System aborts the deletion process.

    Use case ends.

---

### Use Case: U8. Edit Session (with Optional Notes)

**Main Success Scenario:**
1. User specifies the date, time or note to update for a household's session.
2. System checks if the provided session index exists.
3. System validates the new date, time, or note.
4. If valid, system updates the session details.
5. System displays a confirmation message
   
    Use case ends.

**Extensions:**

2a. Invalid session index.
* 2a1. System displays an error message.

    Use case ends.

3a. Invalid date/time format.
* 3a1. System displays an error message.
* 3a2. User corrects input and retries.
Use case resumes from step 1.

3b. A session already exists at the new date/time (**Prevent Double-Booking (U4)**).
Use case resumes from step 1 or ends if canceled.

4a. System fails to save the changes.
* 4a1. System displays an error message.
* 4a2. User retries or exits.
Use case resumes from step 1 or ends if unsuccessful.

---

### Use Case: U9. Clear All Entries

**Main Success Scenario:**
1. User clears all entries.
2. System prompts for confirmation.
3. User confirms the action.
4. System removes all stored data (households, sessions, and notes).
5. System displays a confirmation message.

    Use case ends.

**Extensions:**

2a. User cancels the operation.
* 2a1. System aborts the clearing process.

    Use case ends.

4a. System fails to clear data.
* 4a1. System displays an error message.
* 4a2. User retries or exits.
Use case resumes from step 2 or ends if the issue persists.

--------------------------------------------------------------------------------------------------------------------

### Non-Functional Requirements:

1. All commands (e.g., adding, searching, updating, deleting) must execute within 300 milliseconds under normal conditions.
2. The application must launch within 3 seconds on a standard machine (e.g., Intel i5, 8GB RAM).
3. If bulk operations (e.g., clearing all records) are performed, they must complete within 5 seconds for up to 5,000 entries.
4. The application must consume no more than 200 MB of RAM during normal operation.
5. The system must be fully operable via CLI, with intuitive and minimal keyboard commands to streamline workflows.
6. Error messages must be clear and descriptive, helping users correct mistakes efficiently.
7. The application must support high contrast mode for accessibility.
8. Household records and session data must be automatically saved to prevent loss due to unexpected shutdowns.
9. The system must be able to recover from a crash without losing the last committed state.
10. The application must run on Windows, macOS, and Linux without modification.
11. The system should require only a standard Java Runtime Environment (JRE 11 or later) to function.
12. The system must work without an internet connection and store data locally.
13. The application must be self-contained with minimal dependencies, avoiding unnecessary external libraries.
14. The system must run on machines with at least 2GB RAM, 1GHz CPU, and 200MB disk space.

--------------------------------------------------------------------------------------------------------------------

## Glossary

* Household Record: A digital record containing information about a household, including its address, members, and relevant notes.
* Engagement Session: A scheduled appointment between a social service worker and a household for follow-up, assistance, or other social services.
* CLI (Command Line Interface): A text-based interface that allows users to interact with the system using commands rather than a graphical interface.
* GUI (Graphical User Interface): A visual interface that allows users to interact with the system using graphical elements such as buttons, icons, and windows, as opposed to a text-based interface like a CLI
* RAM (Random Access Memory): A type of computer memory that temporarily stores data and instructions while a program is running, allowing for quick access and processing.
* Tag: A keyword or label assigned to a household record to categorize or identify it easily.
* Double-Booking: A scheduling conflict where two sessions are assigned to the same time slot, which the system should prevent.
* Session History: A chronological log of all past engagement sessions associated with a household.
* Filter: A feature that allows users to refine search results based on specific criteria like name, address, or tags.
* High Contrast Mode: A visual accessibility feature that increases the contrast between text and background to improve readability for users with visual impairments.
* Crash: A case of an unexpected shutdown.
* Automatic save: A feature that saves user data with the last command entered to prevent loss in case of system failure.
* Workflows: A defined sequence of steps or processes that users follow to complete a task efficiently within the system, such as adding a household record or scheduling an engagement session.
* Bulk Operation: A command or function that processes multiple records at once, such as clearing all entries.
* Session Notification: An alert or reminder about an upcoming engagement session, which can be displayed in-app or sent via email.
* Session Data: Information related to a user's current interaction with the system, including active records, scheduled sessions, and temporary changes before they are saved.
* Structured List: A formatted way of displaying household records, showing key details like name, address, and contact information.
* Last Committed State: The most recent version of stored data that was successfully saved, ensuring that no progress is lost in case of an unexpected system crash.
* Dependencies: External software libraries or components required for the application to function properly, such as Java Runtime Environment (JRE) or specific third-party tools.
