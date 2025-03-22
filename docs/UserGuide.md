# Em-Social User Guide

## Introduction
Welcome to Em-Social, a specialized management software designed for social workers. Em-Social streamlines household data management and session scheduling through an intuitive interface, allowing you to focus on what matters most – supporting the communities you serve.

Em-Social enables you to maintain detailed records of households, schedule and track visits, prevent scheduling conflicts, and efficiently organize your caseload through powerful filtering and search capabilities.

- [Em-Social User Guide](#em-social-user-guide)
  - [Introduction](#introduction)
  - [Features](#features)
  - [Usage](#usage)
    - [Running Em-Social](#running-em-social)
  - [Household Management](#household-management)
    - [Add a household](#add-a-household)
    - [Edit a household](#edit-a-household)
    - [Delete a household](#delete-a-household)
    - [List all households](#list-all-households)
    - [Find households](#find-households)
  - [Session Management](#session-management)
    - [Add a session](#add-a-session)
    - [Edit a session](#edit-a-session)
    - [Delete a session](#delete-a-session)
    - [List sessions](#list-sessions)
  - [User Interface Navigation](#user-interface-navigation)
    - [Household panel](#household-panel)
    - [Session panel](#session-panel)
    - [Command box](#command-box)
    - [Result display](#result-display)
  - [General Commands](#general-commands)
    - [Clear data](#clear-data)
    - [Help](#help)
    - [Exiting the program](#exiting-the-program)
  - [Saving the data](#saving-the-data)
  - [Command summary](#command-summary)

## Features
Em-Social offers the following key features:

1. **Household Management**: Add, edit, and categorize household information including name, address, contact details, and tags.
2. **Session Scheduling**: Schedule visits with households without double-booking.
3. **Session Notes**: Add detailed notes for each session to track interactions and follow-up items.
4. **Powerful Searching**: Find households based on tags or search criteria.
5. **Duplicate Prevention**: System ensures no duplicate household entries exist.
6. **Interactive UI**: View households and their sessions side by side.

## Usage
### Running Em-Social
1. Ensure you have Java 11 or above installed on your computer.
2. Download the latest release of Em-Social from the releases page.
3. Double-click the JAR file or run it from the command line using: `java -jar em-social.jar`

## Household Management
Households are the core entities in Em-Social. Each household represents a family or living unit that you work with.

### Add a household
You can add a new household with the `add-household` command.
```
add-household n/HOUSEHOLD_NAME a/ADDRESS p/PHONE_NUMBER [t/TAG]...
```

Example of usage:
```
add-household n/Tan Family a/Blk 30 Geylang Street 29, #06-40 p/91234567 t/elderly t/priority
```

Expected outcome:
```
New household added: Tan Family (ID: H000001)
Address: Blk 30 Geylang Street 29, #06-40
Contact: 91234567
Tags: [elderly][priority]
```

### Edit a household
You can modify household details with the `edit-household` command.
```
edit-household ID [n/HOUSEHOLD_NAME] [a/ADDRESS] [p/PHONE_NUMBER] [t/TAG]...
```

Example of usage:
```
edit-household H000001 n/Tan Family (Urgent) p/98765432
```

Expected outcome:
```
Edited household: Tan Family (Urgent) (ID: H000001)
Address: Blk 30 Geylang Street 29, #06-40
Contact: 98765432
Tags: [elderly][priority]
```

### Delete a household
You can remove a household with the `delete-household` command.
```
delete-household ID
```

Example of usage:
```
delete-household H000001
```

Expected outcome:
```
Deleted household: Tan Family (Urgent) (ID: H000001)
```

### List all households
You can view all households with the `list-households` command.
```
list-households
```

Expected outcome:
```
Listed all households:
1. Tan Family (ID: H000001)
2. Lee Family (ID: H000002)
3. Wong Family (ID: H000003)
```

### Find households
You can search for households with the `find-households` command.
```
find-households KEYWORD [MORE_KEYWORDS]...
```

Example of usage:
```
find-households Tan Lee
```

Expected outcome:
```
2 households found:
1. Tan Family (ID: H000001)
2. Lee Family (ID: H000002)
```

## Session Management
Sessions represent scheduled visits to households.

### Add a session
You can schedule a session with the `add-session` command.
```
add-session id/HOUSEHOLD_ID d/DATE (YYYY-MM-DD) t/TIME (HH:MM)
```

Example of usage:
```
add-session id/H000003 d/2025-03-15 t/14:30
```

Expected outcome:
```
New session added to household H000003:
Date: 2025-03-15
Time: 14:30
```

### Edit a session
You can modify a session with the `edit-session` command. This command also allows you to add or update a note at the same time.
```
edit-session id/HOUSEHOLD_ID-SESSION_INDEX d/DATE t/TIME [n/NOTE]
```

Example of usage (without note):
```
edit-session id/H000003-1 d/2025-03-16 t/15:00
```

Example of usage (with note):
```
edit-session id/H000003-1 d/2025-03-16 t/15:00 n/Follow-up on medical assistance application
```

Expected outcome (without note):
```
Edited session:
Date: 2025-03-16
Time: 15:00
```

Expected outcome (with note):
```
Edited session:
Date: 2025-03-16
Time: 15:00
Note: Follow-up on medical assistance application
```

### Delete a session
You can remove a session with the `delete-session` command.
```
delete-session id/HOUSEHOLD_ID-SESSION_INDEX
```

Example of usage:
```
delete-session id/H000003-1
```

Expected outcome:
```
Deleted session:
Date: 2025-03-16
Time: 15:00
```

### List sessions
You can view all sessions with the `list-sessions` command. Optionally, you can filter sessions by household.
```
list-sessions [id/HOUSEHOLD_ID]
```

Example of usage (all sessions):
```
list-sessions
```

Example of usage (household-specific):
```
list-sessions id/H000003
```

Expected outcome:
```
Listed all sessions for household H000003:
1. Date: 2025-03-15, Time: 14:30
2. Date: 2025-04-01, Time: 10:00
```

## User Interface Navigation
### Household panel
The household panel displays a list of all households. Click on a household to view its details and associated sessions.

### Session panel
The session panel shows sessions for the selected household. Each session card displays the date, time, and any notes associated with the session.

### Command box
Enter commands in the command box at the bottom of the application window. Press Enter to execute the command.

### Result display
The result display shows the outcome of your most recent command, including success messages or error notifications.

## General Commands

### Clear data
You can clear all household and session data with the `clear` command.
```
clear
```

Expected outcome:
```
Address book has been cleared!
```

### Help
You can view a summary of available commands with the `help` command.
```
help
```

### Exiting the program
You can exit Em-Social with the `exit` command.
```
exit
```

## Saving the data
Em-Social automatically saves data to a local file after each command. There is no need to save manually.

## Command summary

| Action | Format | Example |
|--------|--------|---------|
| Add household | `add-household n/HOUSEHOLD_NAME a/ADDRESS p/PHONE_NUMBER [t/TAG]...` | `add-household n/Tan Family a/Blk 30 Geylang Street 29, #06-40 p/91234567 t/elderly` |
| Edit household | `edit-household ID [n/HOUSEHOLD_NAME] [a/ADDRESS] [p/PHONE_NUMBER] [t/TAG]...` | `edit-household H000001 n/Tan Family (Urgent) p/98765432` |
| Delete household | `delete-household ID` | `delete-household H000001` |
| List households | `list-households` | `list-households` |
| Find households | `find-households KEYWORD [MORE_KEYWORDS]...` | `find-households Tan Lee` |
| Add session | `add-session id/HOUSEHOLD_ID d/DATE (YYYY-MM-DD) t/TIME (HH:MM)` | `add-session id/H000003 d/2025-03-15 t/14:30` |
| Edit session | `edit-session id/HOUSEHOLD_ID-SESSION_INDEX d/DATE t/TIME [n/NOTE]` | `edit-session id/H000003-1 d/2025-03-16 t/15:00 n/Follow-up note` |
| Delete session | `delete-session id/HOUSEHOLD_ID-SESSION_INDEX` | `delete-session id/H000003-1` |
| List sessions | `list-sessions [id/HOUSEHOLD_ID]` | `list-sessions id/H000003` |
| Clear data | `clear` | `clear` |
| Help | `help` | `help` |
| Exit | `exit` | `exit` |
