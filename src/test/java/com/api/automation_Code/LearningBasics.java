package com.api.automation_Code;

import com.api.Files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LearningBasics {

    public static String placeID;

    //1. BaseURI has to be present
    //2. given() - all the input details(query parameters, headers, body)
    //3. when() - submissions to the API [resources, http method(post, get, put, delete)]
    //4. then() - validations

    @Test(enabled = true, priority = 1)
    public void mapsAddUpdateRetrievePlaceAPI(){

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        //Action - What did we do in Postman? // We requested to create a placeAPI
        //           baseURL, resources, query parameters, headers, body

        String response = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.addPlace())
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

         System.out.println(response);

         //Action  - Whatever creation we did in Action 1, we want to retrieve and verify our creation is accurate
        //Steps to be taken care
        //Step 1 - place_id consistently will change with every run. So we have to dynamically use it.
        //Step 2 - we have to parse something. Converting input data into a structured format for further processing

        JsonPath js = new JsonPath(response);
        placeID = js.getString("place_id");

        //Action - Change the Address (PUT)

        String newAddress = "2900 Eisenhower Ave";
        given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeID + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));


        String retrievePlaceResponse =  given().queryParam("key", "qaclick123")
                .queryParam("place_id", placeID)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().statusCode(200).extract().response().asString();
        System.out.println(retrievePlaceResponse);


         JsonPath js1 = new JsonPath(retrievePlaceResponse);
         String actualAddress = js1.getString("address");

        Assert.assertEquals(actualAddress, newAddress);

    }




    @Test(enabled = true, priority = 2, dependsOnMethods = "mapsAddUpdateRetrievePlaceAPI")
    public void deletePlaceAPI(){
    RestAssured.baseURI = "https://rahulshettyacademy.com";

    //Action item - We are deleting the Place
    given().queryParam("key", "qaclick123")
            .header("Content-Type", "application/json")
            .body("{\n" +
                    "    \"place_id\":\"" + placeID + "\"\n" +
                    "}")
            .when().delete("/maps/api/place/delete/json")
            .then().assertThat().statusCode(200)
            .body("status", equalTo("OK"));


    //Action item - We are trying to retrieve the place which we just deleted
        String retrievePlaceResponsePostDeletion = given().queryParam("key", "qaclick123")
                .queryParam("place_id", placeID)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().statusCode(404)
                .body("msg", equalTo("Get operation failed, looks like place_id  doesn't exists"))
                .extract().response().asString();

        System.out.println("After deletion, retrieval Response : " +retrievePlaceResponsePostDeletion);
    }
}
