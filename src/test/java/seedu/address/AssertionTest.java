package seedu.address;

public class AssertionTest {
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // This will only execute if assertions are enabled

        if (assertionsEnabled) {
            System.out.println("Assertions are enabled!");
        } else {
            System.out.println("Assertions are NOT enabled!");
        }
    }
}
