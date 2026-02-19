package edu.oosd.restservices.RestApi;

import java.util.Map;

public class Greeting {
    private long id;
    private String content;
    private final Map<String, String> links;


    public Greeting(long id, String content, Map<String, String> links) {
        this.id = id;
        this.content = content;
        this.links = links;
    }

    public long getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public Map<String, String> getLinks() {
        return links;
    }

}
