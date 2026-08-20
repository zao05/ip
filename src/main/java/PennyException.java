public class PennyException extends Exception {

    /**
     * Constructs a PennyException with the specified custom error message.
     * @param message The detailed error message explaining what went wrong.
     */
    public PennyException(String message) {
        super(message);
    }
}