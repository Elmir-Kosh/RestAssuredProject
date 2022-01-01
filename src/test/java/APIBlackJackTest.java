import com.jayway.jsonpath.JsonPath;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;


public class APIBlackJackTest {

    String URLAdress = "https://qa-demo.kdev2.cone.ee/";
    String x_api_key = new pages.Util().getPropertyValue("P_KEY");
    String gameName = "SuperGame";
    String URLforNewGame = "https://qa-demo.kdev2.cone.ee/api/game?gameName=" + gameName + "&gameVersion=";
    int gameVersion = 2;


        @Test
        void checkMainPage() throws IOException {
            given()
                    .get(URLAdress)
                    .then()
                    .statusCode(200);
        }

        @Test
    void keyCheck() throws IOException {

            given()
                    .when ()
                    .contentType (ContentType.JSON)
                    .get (URLAdress+"key-check?apiKey="+ x_api_key)
                    .then()
                    .statusCode(200).log().all();
        }

        @Test
    void createNewGameTest() throws IOException {

            given()
                    .when()
                    .contentType(ContentType.JSON)
                    .header("x-api-key", x_api_key)
                    .post(URLforNewGame + gameVersion)
                    .then()
                    .statusCode(200).log().all();
        }

            @Test
        void getGameInfoTest (){
                String json = given().when().header("x-api-key", x_api_key).post(URLforNewGame + gameVersion).asString();
                String ID = JsonPath.read(json, "$..id").toString();
                String id = ID.substring(2, ID.length() - 2);
                System.out.println("Game id: " + id);

                given()
                    .when()
                    .contentType(ContentType.JSON)
                    .header("x-api-key", x_api_key)
                    .get("https://qa-demo.kdev2.cone.ee/api/game?gameId=" + id)
                    .then()
                    .statusCode(200).log().all();
        }

        @Test
    void giveCardTest() {
            String json = given().when().header("x-api-key", x_api_key).post(URLforNewGame + gameVersion).asString();
            String ID = JsonPath.read(json, "$..id").toString();
            String id = ID.substring(2, ID.length() - 2);
            System.out.println("Game id: " + id);
            String card = "A ♥";
            given()
                    .when()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .header("x-api-key", x_api_key)
                    .post("https://qa-demo.kdev2.cone.ee/api/give-card?gameId=" + id + "&cardText=" + card)
                    .then()
                    .statusCode(200).log().all();
        }

}
