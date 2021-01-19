package controller;

import annotations.GetRequest;

import annotations.PostRequest;
import exceptions.RouteNotFoundException;
import gateway.RequestRouter;
import protocol.Request;
import protocol.RequestMethod;
import protocol.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public abstract class Controller {
    public Response handleRequest(Request request) {
        Class<?> clazz = this.getClass();
        Optional<Method> requestedMethod = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> {
            if (method.isAnnotationPresent(RequestMethod.getMethodClass(request.getMethod()))) {
                switch (request.getMethod()) {
                    case GET:
                        GetRequest getAnnot = (GetRequest) method.getAnnotation(RequestMethod.getMethodClass(request.getMethod()));
                        return (getAnnot.value().equals(request.getTargetRoute()));

                    case ADD:
                        PostRequest postAnnot = (PostRequest) method.getAnnotation(RequestMethod.getMethodClass(request.getMethod()));
                        return (postAnnot.value().equals(request.getTargetRoute()));

                    default:
                        return false;
                }
            } else
                return false;
        }).findAny();
        try {
            return (Response) requestedMethod.orElseThrow(() -> new RouteNotFoundException(request.getTargetRoute())).invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RouteNotFoundException(request.getTargetRoute());
    }
}
