package exceptions;

public class DuplicateEntryException extends IllegalArgumentException {
    public DuplicateEntryException(String id){
        super("Entry with id: " + id +" already exists.");
    }
}
