package exceptions;

public class UnknownCommandException extends IllegalArgumentException {
    public UnknownCommandException(String command){
        super("Command :\"" + command + "\" is unknown. Enter help for help =)");
    }
}
