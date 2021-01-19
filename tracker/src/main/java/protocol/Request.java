package protocol;

import java.io.Serializable;
import java.util.Map;

public  class Request implements Serializable {
    private RequestMethod method;
    private String targetRoute;
    private Map body;


    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getTargetRoute() {
        return targetRoute;
    }

    public void setTargetRoute(String targetRoute) {
        this.targetRoute = targetRoute;
    }

    public Map getBody() {
        return body;
    }

    public void setBody(Map body) {
        this.body = body;
    }

}
