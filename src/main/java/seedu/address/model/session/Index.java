package seedu.address.model.session;

/**
 * Represents a Session's index in the household book.
 * {@code Index} objects are immutable.
 */
public class Index {
    private final int zeroBasedIndex;

    private Index(int zeroBasedIndex) {
        if (zeroBasedIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.zeroBasedIndex = zeroBasedIndex;
    }

    public static Index fromOneBased(int oneBasedIndex) {
        return new Index(oneBasedIndex - 1);
    }

    public static Index fromZeroBased(int zeroBasedIndex) {
        return new Index(zeroBasedIndex);
    }

    public int getZeroBased() {
        return zeroBasedIndex;
    }

    public int getOneBased() {
        return zeroBasedIndex + 1;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Index // instanceof handles nulls
                && zeroBasedIndex == ((Index) other).zeroBasedIndex); // state check
    }

    @Override
    public int hashCode() {
        return zeroBasedIndex;
    }
}
