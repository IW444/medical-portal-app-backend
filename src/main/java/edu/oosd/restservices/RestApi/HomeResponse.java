package edu.oosd.restservices.RestApi;

import java.util.List;
import java.util.Map;

public class HomeResponse {

    private final int status;
    private final String message;
    private final Map<String, String> links;

    public HomeResponse(int status, String message, Map<String, String> links) {
        this.status = status;
        this.message = message;
        this.links = links;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getLinks() {
        return links;
    }
}
