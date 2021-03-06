package techproedenglish02.techproedenglish02api;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;
import org.testng.asserts.SoftAssert;

import TestBaseNtUrls.TestBaseDummy;
import TestData.DummyTestData;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PostRequestNt01 extends TestBaseDummy {

	/*
	 When
	  	I send a POST Request to the Url http://dummy.restapiexample.com/api/v1/create
	  	by using the following Request Body {
											    "name":"SULEYMAN ALP",
											    "salary":"1000",
											    "age":"18",
											    "profile_image": ""
											}
	 Then 
	  	Status code is 200
	  	And response body should be like {
										    "status": "success",
										    "data": {
										        "name": "SULEYMAN ALP",
										        "salary": "1000",
										        "age": "18",
										        "profile_image": null
										    },
										    "message": "Successfully! Record has been added."
										 }
	 */

			/*
			 Most APIs need authorization for POST Request
			*/
	
	@Test
	public void post01() {
		
		//1) Set the URL
		spec.pathParam("createPath", "create");
				
		//2) Set the Request Body
		DummyTestData reqBodyObj = new DummyTestData();
		Map<String, String> reqBodyMap = reqBodyObj.setUpPostReqBodyByUsingMap();
		
		//3) Send the Request
		Response response = given().
								contentType(ContentType.JSON).
								spec(spec).
								auth().
								basic("admin", "password123").
								body(reqBodyMap).
							when().
							    post("/{createPath}");
		
		response.prettyPrint();
		
		
		//4) Assert the things which are used in Request Body
			//1.Way:Hard Assertion ==> Map + JsonPath
			JsonPath json = response.jsonPath();
			
			assertEquals(reqBodyMap.get("name"), json.getString("data.name"));
			assertEquals(reqBodyMap.get("salary"), json.getString("data.salary"));
			assertEquals(reqBodyMap.get("age"), json.getString("data.age"));
	
			if(reqBodyMap.get("profile_image").equals("")) {
				reqBodyMap.put("profile_image", null);
			}
			assertEquals(reqBodyMap.get("profile_image"),json.getString("data.profile_image"));
			
			Map<String, String> expectedMsgMap = reqBodyObj.setUpMessageDataByUsingMap();
			assertEquals(expectedMsgMap.get("status"), json.getString("status"));
			assertEquals(expectedMsgMap.get("message"), json.getString("message"));
		
			//2.Way:Hard Assertion ==> Map + GSON
			Map<String, Object> actualDataMap = response.as(HashMap.class);
			
			assertEquals(reqBodyMap.get("name"),((Map)actualDataMap.get("data")).get("name"));
			assertEquals(reqBodyMap.get("salary"),((Map)actualDataMap.get("data")).get("salary"));
			assertEquals(reqBodyMap.get("age"),((Map)actualDataMap.get("data")).get("age"));
			
//			Note: Following code is used above in the 1.Way, no need to run it again. 
//                because of that I commented it out			
//			if(reqBodyMap.get("profile_image").equals("")) {
//				reqBodyMap.put("profile_image", null);
//			}
			
			assertEquals(reqBodyMap.get("profile_image"),((Map)actualDataMap.get("data")).get("profile_image"));
			
			assertEquals(expectedMsgMap.get("status"),actualDataMap.get("status"));
			assertEquals(expectedMsgMap.get("message"),actualDataMap.get("message"));
			
			//3.Way: Soft assertion ==> JSONObject + JsonPath 
			JSONObject expectedDataJSONObject = reqBodyObj.setUpPostReqBodyByUsingJSONObject();
			
			SoftAssert softAssert = new SoftAssert();
			
			softAssert.assertEquals(json.getString("data.name"), expectedDataJSONObject.getString("name"));
			softAssert.assertEquals(json.getString("data.salary"), expectedDataJSONObject.getString("salary"));
			softAssert.assertEquals(json.getString("data.age"), expectedDataJSONObject.getString("age"));
			
			JSONObject expectedMsgJSONObject = reqBodyObj.setUpMessageDataByUsingJSONObject();
			softAssert.assertEquals(json.getString("status"), expectedMsgJSONObject.getString("status"));
			softAssert.assertEquals(json.getString("message"), expectedMsgJSONObject.getString("message"));
					
			softAssert.assertAll();

	}

}
