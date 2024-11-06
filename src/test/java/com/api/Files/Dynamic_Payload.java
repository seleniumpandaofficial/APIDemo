package com.api.Files;

public class Dynamic_Payload {

    public static String addBookPayload(String isbn, String aisle){
        String payload = "{\n" +
                "\"name\":\"Learn Appium Automation with Java\",\n" +
                "\"isbn\":\"" + isbn + "\",\n" +
                "\"aisle\":\"" + aisle + "\",\n" +
                "\"author\":\"John foe\"\n" +
                "}";

        return payload;
    }
}
