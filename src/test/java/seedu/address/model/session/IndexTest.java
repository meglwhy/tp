package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IndexTest {

    private int validOneBased;
    private int validZeroBased;

    @BeforeEach
    void setUp() {
        // Set up a valid index. For example, one-based index 5 means zero-based is 4.
        validOneBased = 5;
        validZeroBased = validOneBased - 1;
    }

    @Test
    void fromOneBased_validIndex_returnsCorrectIndex() {
        Index index = Index.fromOneBased(validOneBased);
        // Verify that the underlying zero-based index is one less than the one-based input.
        assertEquals(validZeroBased, index.getZeroBased());
        // Verify that the one-based getter returns the correct value.
        assertEquals(validOneBased, index.getOneBased());
    }

    @Test
    void fromZeroBased_validIndex_returnsCorrectIndex() {
        Index index = Index.fromZeroBased(validZeroBased);
        // Verify that the stored zero-based index is as expected.
        assertEquals(validZeroBased, index.getZeroBased());
        // Verify that the one-based getter returns zeroBasedIndex + 1.
        assertEquals(validOneBased, index.getOneBased());
    }

    @Test
    void fromOneBased_invalidIndex_throwsException() {
        // A one-based index of 0 would lead to a negative zero-based index, which should throw an exception.
        assertThrows(IndexOutOfBoundsException.class, () -> Index.fromOneBased(0));
    }

    @Test
    void fromZeroBased_invalidIndex_throwsException() {
        // Negative zero-based index should throw an exception.
        assertThrows(IndexOutOfBoundsException.class, () -> Index.fromZeroBased(-1));
    }

    @Test
    void equalsAndHashCode_correctness() {
        // Create two Index objects representing the same value (one-based 3 and zero-based 2).
        Index indexA = Index.fromOneBased(3);
        Index indexB = Index.fromZeroBased(2);

        // Reflexive: object equals itself.
        assertTrue(indexA.equals(indexA));
        // Symmetric: indexA equals indexB and vice versa.
        assertTrue(indexA.equals(indexB));
        assertTrue(indexB.equals(indexA));
        // Consistent hash code: equal objects must have the same hash code.
        assertEquals(indexA.hashCode(), indexB.hashCode());
    }
}

