package org.example;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class ExampleMain {

    private final String apiKey = "98553194bb3449e1b213eb7196c48bb9";


    // Get_________________________________

    @Test
    void getRecipePositiveTest() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("diet", "Cannibal")
                .queryParam("includeHuman", 100)
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .statusCode(200);
    }

    @Test
    void getRecipeNegativeTest() {
        given()
                .queryParam("apiKey", apiKey)
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch2")
                .then()
                .statusCode(404);
    }

    @Test
    void getRecipeWithBodyChecksNameAndOtherTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .when()
                .get("https://api.spoonacular.com/recipes/794527/information")
                .body()
                .jsonPath();
        assertThat(response.get("vegetarian"), is(true));
        assertThat(response.get("creditsText"), equalTo("Jen West"));
        assertThat(response.get("lowFodmap"), is(false));
        assertThat(response.get("title"), equalTo("Chia Yogurt Apricot Bowl"));
    }

    @Test
    void getRecipeWithBodyChecksManyQueryParamsTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("number", 50)
                .queryParam("sort", "trans-fat")
                .queryParam("offset", 100)
                .queryParam("titleMatch", "Chicken")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"), equalTo(439));
    }

    @Test
    void getRecipeManyQueryParamsTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("minIron", 2)
                .queryParam("minSelenium", 2)
                .queryParam("minZinc", 2)
                .queryParam("minIodine", 2)
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"), equalTo(1249));
        assertThat(response.get("results.title[1]"), containsString("Chicken"));
    }

    //Post__________________

    @Test
    void postCuisineFTWTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("title", "Pork roast with green beans")
                .queryParam("ingredientList", "3 oz pork shoulder")
                .then()
                .statusCode(200)
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .jsonPath();
        assertThat(response.get("cuisine").toString(), equalTo("Mediterranean"));
        assertThat(response.get("cuisines").toString(), containsString("Mediterranean"));
        assertThat(response.get("cuisines").toString(), containsString("European"));
        assertThat(response.get("cuisines").toString(), containsString("Italian"));
        assertThat(response.get("confidence").toString(), equalTo("0.0"));
    }



    @Test
    void addShopTest() {
        String id = given()
                .queryParam("hash", "dc5d1260afa5d93e8e6c3595d0ef7678465c2451")
                .queryParam("apiKey", apiKey)
                .body("{\n" +
                        "\t\"item\": \"1 package baking powder\",\n" +
                        "\t\"aisle\": \"Baking\",\n" +
                        "\t\"parse\": true\n" +
                        "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/vacuumpz0/shopping-list/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        JsonPath response = given()
                .queryParam("hash", "dc5d1260afa5d93e8e6c3595d0ef7678465c2451")
                .queryParam("apiKey", apiKey)
                .when()
                .get("https://api.spoonacular.com/mealplanner/vacuumpz0/shopping-list")
                .body()
                .prettyPeek()
                .jsonPath();

        assertThat(response.get("aisles.aisle[0]").toString(), equalTo("Baking"));
        assertThat(response.get("aisles.items.name[0]").toString(), containsString("baking powder"));
        assertThat(response.get("aisles.items.id[0]").toString(), containsString(id));


                given()
                .queryParam("hash", "dc5d1260afa5d93e8e6c3595d0ef7678465c2451")
                .queryParam("apiKey", apiKey)
                .delete("https://api.spoonacular.com/mealplanner/vacuumpz0/shopping-list/items/" + id)
                .then()
                .statusCode(200);


    }


//    @Test
//    void addMealTest() {
//        String id = given()
//                .queryParam("hash", "dc5d1260afa5d93e8e6c3595d0ef7678465c2451")
//                .queryParam("apiKey", apiKey)
//                .body("[\n" +
//                        "    {\n" +
//                        "        \"date\": 1589500800,\n" +
//                        "        \"slot\": 1,\n" +
//                        "        \"position\": 0,\n" +
//                        "        \"type\": \"INGREDIENTS\",\n" +
//                        "        \"value\": {\n" +
//                        "            \"ingredients\": [\n" +
//                        "                {\n" +
//                        "                    \"name\": \"1 banana\"\n" +
//                        "                },\n" +
//                        "                {\n" +
//                        "                    \"name\": \"coffee\",\n" +
//                        "                    \"unit\": \"cup\",\n" +
//                        "                    \"amount\": \"1\",\n" +
//                        "                    \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/brewed-coffee.jpg\"\n" +
//                        "                },\n" +
//                        "            ]\n" +
//                        "        }\n" +
//                        "    },\n" +
//                        "    {\n" +
//                        "        \"date\": 1589500800,\n" +
//                        "        \"slot\": 2,\n" +
//                        "        \"position\": 0,\n" +
//                        "        \"type\": \"CUSTOM_FOOD\",\n" +
//                        "        \"value\": {\n" +
//                        "            \"id\": 348,\n" +
//                        "            \"servings\": 1,\n" +
//                        "            \"title\": \"Aldi Spicy Cashews - 30g\",\n" +
//                        "            \"image\": \"https://spoonacular.com/cdn/ingredients_100x100/cashews.jpg\"\n" +
//                        "        }\n" +
//                        "    }\n" +
//                        "]")
//                .when()
//                .post("https://api.spoonacular.com/mealplanner/vacuumpz0/items")
////                .prettyPeek()
//                .then()
//                .statusCode(200)
//                .extract()
//                .jsonPath()
//                .get("id")
//                .toString();
//
//        given()
//                .queryParam("hash", "dc5d1260afa5d93e8e6c3595d0ef7678465c2451")
//                .queryParam("apiKey", apiKey)
//                .delete("https://api.spoonacular.com/mealplanner/vacuumpz0/items/" + id)
//                .then()
//                .statusCode(200);
//    }




}