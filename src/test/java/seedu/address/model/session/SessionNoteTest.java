package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionNoteTest {

    @Test
    public void constructor_validNote_success() {
        String validNoteText = "This is a valid note.";
        SessionNote note = new SessionNote(validNoteText);
        // Verify the note's value and toString() output
        assertEquals(validNoteText, note.value);
        assertEquals(validNoteText, note.toString());
    }

    @Test
    public void constructor_invalidNote_throwsException() {
        // Notes that are blank or start with whitespace are invalid.
        String invalidNote = " ";
        assertThrows(IllegalArgumentException.class, () -> new SessionNote(invalidNote));

        String emptyNote = "";
        assertThrows(IllegalArgumentException.class, () -> new SessionNote(emptyNote));
    }

    @Test
    public void equalsAndHashCode_sameNotes_equal() {
        String noteText = "A sample note";
        SessionNote note1 = new SessionNote(noteText);
        SessionNote note2 = new SessionNote(noteText);
        assertEquals(note1, note2);
        assertEquals(note1.hashCode(), note2.hashCode());
    }
}

