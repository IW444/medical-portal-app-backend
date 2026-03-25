//This is a wrapper class to return:
// HTTP status, message about what happened, and a map of links
// all in a JSON file

package edu.oosd.restservices.RestApi;

import java.util.Map;

public class HomeResponse {

    //Reminder to self:  final means that the value set in the constructor cannot change.
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