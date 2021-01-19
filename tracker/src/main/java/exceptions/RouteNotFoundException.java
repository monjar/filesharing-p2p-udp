package exceptions;

public class RouteNotFoundException extends IllegalArgumentException {
    public RouteNotFoundException(String name){
        super("Route " + name + " is not found.");
    }
}
