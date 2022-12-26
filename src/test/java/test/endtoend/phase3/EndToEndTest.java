/* Program Name	: Phase_3.java
 * Date			: 11/12/2022
 * Purpose		: Phase 3 assessment test
 * Author		: Lasantha Pieries 
 **********************************************************/
package test.endtoend.phase3;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	// Declare Class level variables
	Response response;
	RequestSpecification request;
	String uri = "http://localhost:3000";
	String end_point = "employees";
	
	@Test
	public void Test1() {
		// Create Hash Map object and variable for employee id
		Map<String, Object> MapObj = new HashMap<String, Object>();
		int emp_id;
		
		// Get all employees and validate for status code 200
		Assert.assertEquals(GetAllEmployees().getStatusCode(), 200);
		
		/* Create an employee with name as John and salary as 8000, 
		   then validate the response code is 201. Fetch the employee
		   id of the newly created employee.
		*/
		MapObj.put("name", "John");
		MapObj.put("salary", "8000");
		Assert.assertEquals(CreateEmployee(MapObj).getStatusCode(), 201);
		emp_id = response.jsonPath().getInt("id");
		
		// Get single employee created earlier and validate
		// the name is John and status code is 200.
		Assert.assertTrue(GetSingleEmployee(emp_id).body().asString().contains("John"));
		Assert.assertEquals(response.getStatusCode(), 200);

		// Update the employee created earlier to change the name 
		// to Smith	and validate the status code is 200.
		MapObj.put("id", emp_id);
		MapObj.put("name", "Smith");
		MapObj.put("salary", "8000");
		Assert.assertEquals(UpdateEmployee(MapObj).getStatusCode(), 200);

		// Get single employee name changed to "Smith" earlier
		// and validate the name is Smith and status code is 200.
		Assert.assertTrue(GetSingleEmployee(emp_id).body().asString().contains("Smith"));
		Assert.assertEquals(response.getStatusCode(), 200);
		
		// Delete the employee created earlier and validate 
		// the status code is 200.
		Assert.assertEquals(DeleteEmployee(emp_id).getStatusCode(), 200);

		// Get single employee created earlier and validate
		// the status code is 404.
		Assert.assertEquals(GetSingleEmployee(emp_id).getStatusCode(), 404);

		// Get All Employees and validate that deleted employee
		// earlier is not present in the response.
		Assert.assertFalse(GetAllEmployees().body().asString().contains("Smith"));
	}
	
	public Response GetAllEmployees(){
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		response = request.get(end_point);
		return response ;
	}
	
	public Response GetSingleEmployee(int emp_id) {
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		response = request.get(end_point+"/"+emp_id);
		return response ;
	}
	
	public Response CreateEmployee(Map<String, Object> mapObj) {
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		response = request
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(mapObj)
				.post(end_point+"/create");
		return response ;
	}
	
	public Response UpdateEmployee(Map<String, Object> mapObj) {
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		response = request
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(mapObj)
				.put(end_point+"/"+mapObj.get("id"));
		return response ;
	}	
	
	public Response DeleteEmployee(int emp_id) {
		RestAssured.baseURI = uri;
		request = RestAssured.given();
		response = request.delete(end_point+"/"+emp_id);
		return response;
	}

}
