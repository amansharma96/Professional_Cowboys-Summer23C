package memoranda.util.training;

public class DuplicateEntryException extends Throwable {
    public DuplicateEntryException() {
        super("This gym member is already in the system");
    }
}
