package protocol;

import annotations.GetRequest;
import annotations.PostRequest;
import com.google.gson.annotations.SerializedName;
import exceptions.InvalidRequestMethodException;

import java.lang.annotation.Annotation;

public enum RequestMethod {

    @SerializedName("GET")
    GET,
    @SerializedName("ADD")
    ADD,
    @SerializedName("UPDATE")
    UPDATE;

    public static Class<? extends Annotation> getMethodClass(RequestMethod method) {
        switch (method) {
            case ADD:
                return PostRequest.class;
            case GET:
                return GetRequest.class;
            default:
                throw new InvalidRequestMethodException(method.name());
        }
    }
}
