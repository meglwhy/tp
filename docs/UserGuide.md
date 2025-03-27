# Em-Social User Guide

## Introduction
Welcome to Em-Social, a specialized management software designed for social workers in Singapore. Em-Social streamlines household data management and session scheduling through an intuitive interface, allowing you to focus on what matters most – supporting the communities you serve.

Em-Social enables you to maintain detailed records of households, schedule and track visits, prevent scheduling conflicts, and efficiently organize your caseload through powerful filtering and search capabilities.

### Who is Em-Social for?
Em-Social is designed specifically for social workers who:
1. Manage multiple household cases simultaneously
2. Need to schedule and track regular home visits
3. Require a simple way to maintain case notes and follow-ups
4. Work in community service centers or family service centers
5. Need a lightweight solution that works offline

### Assumptions about our users
We assume our users:
1. Are comfortable with basic typing and text-based commands
2. Have basic computer literacy (can navigate files, install programs)
3. Prefer keyboard-based workflows over extensive mouse navigation
4. Need to quickly enter and retrieve information during busy workdays
5. May have limited technical support in their workplace

If you're already familiar with similar applications, skip to [Quick Start](#quick-start) for setup instructions.

## Table of Contents
- [Em-Social User Guide](#em-social-user-guide)
  - [Introduction](#introduction)
    - [Who is Em-Social for?](#who-is-em-social-for)
    - [Assumptions about our users](#assumptions-about-our-users)
  - [Quick Start](#quick-start)
  - [User Interface Overview](#user-interface-overview)
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
    - [View household sessions](#view-household-sessions)
  - [General Commands](#general-commands)
    - [Clear data](#clear-data)
    - [Help](#help)
    - [Exiting the program](#exiting-the-program)
  - [Tips for Effective Use](#tips-for-effective-use)
  - [Frequently Asked Questions](#frequently-asked-questions)
  - [Glossary](#glossary)
  - [Command Summary](#command-summary)

## Quick Start

1. Ensure you have Java 11 or above installed on your computer by running:
   ```
   java -version
   ```
   If you require installation, refer to this [Java Installation Guide](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)

2. Download the [latest release of Em-Social](https://github.com/AY2425S2-CS2103T-F10-2/tp/releases) from the releases page.

3. Double-click the downloaded JAR file or run it from the command line using:
   ```
   java -jar em-social.jar
   ```

4. The application will start with sample data to help you get familiar with the interface.

5. Type `help` in the command box and press Enter to see available commands.

## User Interface Overview

![User Interface with Labels](images/labeled-ui.png)

The Em-Social interface consists of four main sections:

1. **Household Panel** - Displays all households in your database
2. **Session Panel** - Shows sessions for the selected household
3. **Command Box** - Where you type commands
4. **Result Display** - Shows feedback from your commands

## Household Management
Households are the core entities in Em-Social. Each household represents a family or living unit that you work with.

### Add a household
You can add a new household with the `add-household` command.
```
add-household n/HOUSEHOLD_NAME a/ADDRESS p/PHONE_NUMBER [t/TAG]...
```

Parameters:
- `n/HOUSEHOLD_NAME`: Name of the household (e.g., family name)
- `a/ADDRESS`: Physical address of the household
- `p/PHONE_NUMBER`: Contact number for the household

Example of usage:
```
add-household n/Tan Family a/Blk 30 Geylang Street 29, #06-40 p/91234567
```

Expected outcome:
```
New household added: Household H000012: Tan Family at Blk 30 Geylang Street 29, #06-40 (Contact: 91234567)
```

![add-household success message](images/add-household.png)

> **Tip**: Use meaningful tags to categorize households for easier filtering later. Common tags might include "elderly", "children", "financial-assistance", or "priority".

### Edit a household
You can modify household details with the `edit-household` command.
```
edit-household id/HOUSEHOLD_ID [n/HOUSEHOLD_NAME] [a/ADDRESS] [p/PHONE_NUMBER] [t/TAG]...
```

Parameters:
- `ID`: The household ID (e.g., H000001)
- `[n/HOUSEHOLD_NAME]`: Optional new name for the household
- `[a/ADDRESS]`: Optional new address
- `[p/PHONE_NUMBER]`: Optional new contact number
- `[t/TAG]...`: Optional tags (will replace all existing tags)

Example of usage:
```
edit-household id/H000001 n/Tan Family Urgent p/98765432
```

Expected outcome:
```
Edited Household: Household H000001: Tan Family Urgent at Blk 30 Geylang Street 29, #06-40 (Contact: 98765432)
```

> **Warning**: Editing a household to match an existing household will be rejected to prevent duplicates.

### Delete a household
You can remove a household with the `delete-household` command.
```
delete-household id/HOUSEHOLD_ID
```

Example of usage:
```
delete-household id/H000013
```

The following confirmation dialog box will appear:
![delete-household warning message](images/delete-confirmation.png)

This will display the following outcome upon confirmation:
```
Deleted Household: Household H000013: Tan Family Urgent at Blk 30 Geylang Street 29, #06-40 (Contact: 98765432)
```

> **Warning**: Deleting a household will also delete all associated sessions. This action cannot be undone.

### List all households
You can view all households with the `list-households` command.
```
list-households
```

Expected outcome (System Message):
```
Listed all households.
Total households: 4
```

> **Tip**: Use this command to refresh your view after searching or filtering.

### Find households
You can search for households with the `find-household` command.
Use double quotes to search for a key phrase.

```
find-household KEYWORD [MORE_KEYWORDS]...
```

Parameters:
- `KEYWORD`: Search term to match against household names, addresses, or tags
- Use double quotes for exact phrase matching: `"Tan Family"`

Example of usage (without phrase matching):
```
find-household Tan Lee
```

Example of usage (with phrase matching):
```
find-household "Tan Family"
```

Expected outcome:
```
Found 2 household(s) matching: Tan Lee
```

Expected outcome (with keyphrase):
```
1 households found:
1. Tan Family (ID: H000001)
```
> **Tip**: The search is case-insensitive and matches partial words. For example, "Tan" will match "Tan Family" and "Tanaka".

## Session Management
Sessions represent scheduled visits or meetings with households.

### Add a session
You can schedule a session with the `add-session` command.
```
add-session id/HOUSEHOLD_ID d/DATE tm/TIME
```

Parameters:
- `id/HOUSEHOLD_ID`: The household ID (e.g., H000001)
- `d/DATE`: Date in YYYY-MM-DD format
- `tm/TIME`: Time in 24-hour format (HH:MM)

Example of usage:
```
add-session id/H000001 d/2025-05-15 tm/14:30
```

Expected outcome:
```
New session added to household H000012: Session for H000012 on 2025-05-15 at 14:30
```

> **Warning**: The system will prevent double-booking if you already have another session scheduled at the same time.

### Edit a session
You can modify a session with the `edit-session` command.
```
edit-session id/HOUSEHOLD_ID-SESSION_INDEX d/DATE tm/TIME [n/NOTE]
```

Parameters:
- `id/HOUSEHOLD_ID-SESSION_INDEX`: Household ID and session index (e.g., H000001-1)
- `d/DATE`: New date in YYYY-MM-DD format
- `tm/TIME`: New time in 24-hour format (HH:MM)
- `[n/NOTE]`: Optional note about the session

Example of usage (with note):
```
edit-session id/H000003-1 d/2025-03-16 tm/15:00 n/Follow-up on medical assistance application
```

Expected outcome:
```
Edited session:
Date: 2025-03-16
Time: 15:00
Note: Follow-up on medical assistance application
```

![edit-session success message](images/edit-session.png)

> **Tip**: Use session notes to record key discussion points, action items, or observations during your visit.

### Delete a session
You can remove a session with the `delete-session` command.
```
delete-session id/HOUSEHOLD_ID-SESSION_INDEX
```

Example of usage:
```
delete-session id/H000001-1
```

Expected outcome:
```
Deleted session 1 from household H000001: Session for H000001 on 2025-03-16 at 15:00
```

### List sessions
You can view all sessions for a specific household with the `list-sessions` command.
```
list-sessions id/HOUSEHOLD_ID
```

Example of usage:
```
list-sessions id/H000001
```

Expected outcome:
```
Listed all sessions for household H000001:
1. Date: 2025-03-15, Time: 14:30
2. Date: 2025-04-01, Time: 10:00
```

![list-sessions success message](images/list-session.png)

### View household sessions
You can switch to view all existing sessions for a household using the `view-household-sessions` command.
```
view-household-sessions id/HOUSEHOLD_ID
```

Example of usage:
```
view-household-sessions id/H000001
```

Expected outcome:
```
Viewing sessions for household: H000001
```


## User Interface Navigation
![user interface](images/user-interface.png)

### 1 Household panel
The household panel displays household information such as name, address, contact number and any tags. Click on a household to view its session panel.

### 2 Session panel
The session panel displays session information for the selected household. This includes session dates, times, and any session notes.

### 3 Command box
Enter commands in the command box of the application window. Press Enter/Return to execute the command.

### 4 Feedback display
The feedback display shows the outcome of your most recent command, primarily success messages or error notifications.

## General Commands

### Clear data
You can clear all household and session data with the `clear` command.
```
clear
```

The following confirmation dialog box will appear:
![clear-command confirmation](images/clear-confirmation.png)

This will display the following outcome upon confirmation:
```
All entries have been cleared.
```

> **Warning**: This action will delete ALL households and sessions. It cannot be undone.

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

## Tips for Effective Use

## Command summary

2. **Tagging Strategy**
   - Use consistent tags across households for better organization
   - Consider tags for case type (e.g., `financial`, `medical`, `housing`)
   - Use tags for priority levels (e.g., `urgent`, `follow-up`, `completed`)
   - Add tags for special needs (e.g., `elderly`, `children`, `disability`)

3. **Session Planning**
   - Schedule sessions at least 30 minutes apart to allow for travel time
   - Add detailed notes immediately after sessions while details are fresh
   - Use the list-sessions command to review your upcoming schedule

4. **Data Management**
   - Regularly back up your data file (located at `data/householdbook.json`)
   - Consider creating separate backups for different periods (monthly/quarterly)
   - Review and clean up old or completed cases periodically

5. **Efficient Workflows**
   - Group visits by geographic area to minimize travel time
   - Use the find-household command with area names to identify nearby cases
   - Add time estimates to session notes for better future planning

## Frequently Asked Questions

**Q: Can I import data from other case management systems?**  
A: Currently, Em-Social doesn't support direct imports. You'll need to manually enter household information.

**Q: How many households can Em-Social handle?**  
A: Em-Social can efficiently manage hundreds of households, though performance may decrease with extremely large datasets (1000+).

**Q: Is my data secure?**  
A: Em-Social stores all data locally on your computer. No data is sent to external servers. For sensitive data, ensure your computer is secured with a password and consider encrypting your drive.

**Q: Can multiple social workers use Em-Social simultaneously?**  
A: Em-Social is designed for individual use. For team settings, each social worker should use their own instance of the application.

**Q: What happens if I accidentally delete a household?**  
A: Unfortunately, there's no built-in recovery for deleted households. This is why Em-Social asks for confirmation before deletion. Consider regular backups of your data file.

**Q: Can I customize the fields for households?**  
A: The current version has fixed fields. Future versions may include customizable fields based on user feedback.

**Q: How do I report bugs or request features?**  
A: Please submit issues on our [GitHub repository](https://github.com/AY2425S2-CS2103T-F10-2/tp/issues).

## Glossary

* **Household**: A family or living unit that receives social services, represented as a single entity in Em-Social.
* **Session**: A scheduled appointment between a social worker and a household for follow-up, assistance, or other social services.
* **Tag**: A keyword or label assigned to a household record to categorize or identify it easily.
* **Double-Booking**: A scheduling conflict where two sessions are assigned to the same time slot, which Em-Social prevents automatically.
* **Case Notes**: Detailed information recorded during or after a session, documenting observations, actions taken, and follow-up items.
* **ID**: A unique identifier automatically assigned to each household (format: H######) or session.
* **Command Box**: The text input area at the bottom of the application where you type commands.
* **Result Display**: The area that shows feedback after executing a command.
* **Household Panel**: The left section of the interface that displays the list of households.
* **Session Panel**: The right section of the interface that shows sessions for the selected household.

## Command Summary

| Action | Format | Example |
|--------|--------|---------|
| Add household | `add-household n/HOUSEHOLD_NAME a/ADDRESS p/PHONE_NUMBER [t/TAG]...` | `add-household n/Tan Family a/Blk 30 Geylang Street 29, #06-40 p/91234567 t/elderly` |
| Edit household | `edit-household ID [n/HOUSEHOLD_NAME] [a/ADDRESS] [p/PHONE_NUMBER] [t/TAG]...` | `edit-household id/H000001 n/Tan Family (Urgent) p/98765432` |
| Delete household | `delete-household ID` | `delete-household id/H000001` |
| List households | `list-households` | `list-households` |
| Find households | `find-household KEYWORD [MORE_KEYWORDS]...` | `find-household Tan Lee` |
| Add session | `add-session id/HOUSEHOLD_ID d/DATE tm/TIME` | `add-session id/H000001 d/2025-03-15 tm/14:30` |
| Edit session | `edit-session id/HOUSEHOLD_ID-SESSION_INDEX d/DATE tm/TIME [n/NOTE]` | `edit-session id/H000001-1 d/2025-03-16 tm/15:00 n/Follow-up note` |
| Delete session | `delete-session id/HOUSEHOLD_ID-SESSION_INDEX` | `delete-session id/H000001-1` |
| List sessions | `list-sessions id/HOUSEHOLD_ID` | `list-sessions id/H000001` |
| View household sessions | `view-household-sessions id/HOUSEHOLD_ID` | `view-household-sessions id/H000001` |
| Clear data | `clear` | `clear` |
| Help | `help` | `help` |
| Exit | `exit` | `exit` |

> **Note**: Parameters in UPPERCASE are required, while those in [square brackets] are optional.
