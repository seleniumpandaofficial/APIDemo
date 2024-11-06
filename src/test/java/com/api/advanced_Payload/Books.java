package com.api.advanced_Payload;

import com.api.Files.Dynamic_Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Books {

//using the isbn and aisle value for the first time and validating the response
//using the same isbn and aisle value and validating the response

    @Test(priority = 1,dataProvider = "getData")
    public void addingBooksUsingISBNandAISLEforthefirsttime(String isbn, String aisle){
        RestAssured.baseURI = "http://216.10.245.166";
       String response =  given().header("Content-Type", "application/json")
                .body(Dynamic_Payload.addBookPayload(isbn, aisle))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200).extract().response().asString();
        System.out.println("The response is : " + response);

       JsonPath js = new JsonPath(response);
       String ID = js.getString("ID");
       String msg = js.getString("Msg");
       System.out.println(ID);
        System.out.println(msg);

        //Assertion - Homework
    }

    @Test(priority = 2)
    public void addingBooksUsingISBNandAISLEforthesecondtime(){
        RestAssured.baseURI = "http://216.10.245.166";
        String response =  given().header("Content-Type", "application/json")
                .body(Dynamic_Payload.addBookPayload("uvwxyy", "998888"))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200).extract().response().asString();
        System.out.println("The response is : " + response);

        JsonPath js = new JsonPath(response);
        String ID = js.getString("ID");
        String msg = js.getString("Msg");
        System.out.println(ID);
        System.out.println(msg);

        //Assertion - Homework
    }

    @DataProvider
    public Object[][] getData(){
        Object[][] data = { {"abcde", "23456"},
                            {"bcdef", "23457"},
                            {"cdefg", "23458"},
                            {"abcdedf", "234561"},
                            {"abcdefg", "234436"},
                            {"abcdesds", "1123456"}};
        return data;

    }
}
