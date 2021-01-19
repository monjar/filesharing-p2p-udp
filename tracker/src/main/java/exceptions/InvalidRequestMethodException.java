package exceptions;

public class InvalidRequestMethodException extends IllegalArgumentException{
    public InvalidRequestMethodException(String name){
        super("Method " + name + " is not valid.");
    }
}
