package Utility.JIRA;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.atlassian.httpclient.api.HttpClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import java.net.URL;

/**
 * Utility class for Zephyr REST API
 * @author Rachit Kumar Rastogi
 *
 */

public class ZephyrUtilities {

	static final String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	
	String apiKey, secretKey, userName;
/*
	String defaultAccessKey = "amlyYTo1MzYxZTYwNy1kMmRjLTQzYTItYjg3MC1iM2MwYjRjNjNmMjQgcmFjaGl0a3VtYXIucmFzdG9naSBVU0VSX0RFRkFVTFRfTkFNRQ";
    String defaultSecretKey = "-ZclG5mEB1SJgpw3FuWwvgUqkVYxFcjWQNNkfcLnT34";
    String defaultUserName = "rachitkumar.rastogi";
*/
	String defaultAccessKey = "amlyYTo1MzYxZTYwNy1kMmRjLTQzYTItYjg3MC1iM2MwYjRjNjNmMjQgbXNndGVzdGluZyBVU0VSX0RFRkFVTFRfTkFNRQ";
    String defaultSecretKey = "Ftxkea9n9kLNL5isxw-tFXDZp4FQ8QO4f9_ejikR6bI";
    String defaultUserName = "MSGTesting";
	
/**
 * Default Constructor
 */
	
public ZephyrUtilities()
{
	this.apiKey = defaultAccessKey;
	this.secretKey = defaultSecretKey;
	this.userName = defaultUserName;
}

/**
 * Overloaded Constructor
 * @param apiKey
 * @param secretKey
 * @param userName
 */
public ZephyrUtilities(String apiKey, String secretKey, String userName)
{
	this.apiKey = apiKey;
	this.secretKey = secretKey;
	this.userName = userName;
}

/**
 * Get JSON Web Token (JWT) for GET request, validity 6 minutes.
 * @param Resource
 * @return jwt
 * @throws URISyntaxException
 */
public String getJwt(String Resource) throws URISyntaxException
{
    ZephyrCloudRestClient client = ZephyrCloudRestClient.restBuilder(zephyrBaseUrl, this.apiKey, this.secretKey, this.userName).build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    return jwtGenerator.generateJWT("GET", new URI(zephyrBaseUrl + Resource), 360);    
}

/**
 * Get JSON Web Token (JWT) for POST request, validity 6 minutes
 * @param Resource
 * @return jwt
 * @throws URISyntaxException
 */
public String postJwt(String Resource) throws URISyntaxException
{
    ZephyrCloudRestClient client = ZephyrCloudRestClient.restBuilder(zephyrBaseUrl, this.apiKey, this.secretKey, this.userName).build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    return jwtGenerator.generateJWT("POST", new URI(zephyrBaseUrl + Resource), 360);    
}

/**
 * Get JSON Web Token (JWT) for PUT request, validity 6 minutes
 * @param Resource
 * @return jwt
 * @throws URISyntaxException
 */
public String putJwt(String Resource) throws URISyntaxException
{
    ZephyrCloudRestClient client = ZephyrCloudRestClient.restBuilder(zephyrBaseUrl, this.apiKey, this.secretKey, this.userName).build();
    JwtGenerator jwtGenerator = client.getJwtGenerator();
    return jwtGenerator.generateJWT("PUT", new URI(zephyrBaseUrl + Resource), 360);    
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String[]> getExecutionsForCycle(String resource) throws URISyntaxException
{
	Map<String, String[]> executionIds = new HashMap<String, String[]>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return executionIds;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			String ExecutionData[] = new String[4];
			JSONObject jobj2 = jobj.getJSONObject("execution");
			String executionId = jobj2.getString("id");
			ExecutionData[0] = String.valueOf(jobj2.getLong("issueId"));
			ExecutionData[1] = jobj.getString("issueKey");
			ExecutionData[2] = jobj.getString("issueSummary");
			ExecutionData[3] = String.valueOf(jobj2.getJSONObject("status").getInt("id"));
			executionIds.put(executionId, ExecutionData);
		}
	}
	
	return executionIds;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String> getAllIssuesAssignedToACycle(String resource) throws URISyntaxException
{
	Map<String, String> issueNames = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return issueNames;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			JSONObject jobj2 = jobj.getJSONObject("execution");
			issueNames.put(jobj.getString("issueSummary"), String.valueOf(jobj2.getLong("issueId")));
		}
	}	
	return issueNames;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String> getAllIssueSummaryAndKeysAssignedToACycle(String resource) throws URISyntaxException
{
	Map<String, String> issueNames = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return issueNames;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			issueNames.put(jobj.getString("issueSummary"), jobj.getString("issueKey"));
		}
	}	
	return issueNames;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String> getAllIssueKeysAndSummaryAssignedToACycle(String resource) throws URISyntaxException
{
	Map<String, String> issueNames = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return issueNames;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			issueNames.put(jobj.getString("issueKey"), jobj.getString("issueSummary"));
		}
	}	
	return issueNames;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String> getIssueIdKeyMapping(int projectId, int versionId, String resource) throws URISyntaxException
{
	Map<String, String> myMappingList = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return myMappingList;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			JSONObject jobj2 = jobj.getJSONObject("execution");
			myMappingList.put(String.valueOf(jobj2.getLong("issueId")), jobj.getString("issueKey"));
		}
	}
	
	return myMappingList;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String> getIssueKeyIdMapping(int projectId, int versionId, String resource) throws URISyntaxException
{
	Map<String, String> myMappingList = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return myMappingList;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			JSONObject jobj2 = jobj.getJSONObject("execution");
			myMappingList.put(jobj.getString("issueKey"), String.valueOf(jobj2.getLong("issueId")));
		}
	}
	
	return myMappingList;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 * @throws JSONException
 * @throws ParseException
 */
public Map<String, String> getExecutionsByCycleId(int projectId, int versionId, String resource) throws URISyntaxException, JSONException, ParseException 
	{
	Map<String, String> executionIds = new HashMap<String, String>();
	
	String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(string1);
		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		// System.out.println(IssuesArray.length());
		if (IssuesArray.length() == 0) {
			return executionIds;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			JSONObject jobj2 = jobj.getJSONObject("execution");
			String executionId = jobj2.getString("id");
			long IssueId = jobj2.getLong("issueId");
			executionIds.put(executionId, String.valueOf(IssueId));
		}
	}
	return executionIds;
}

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @return
 * @throws URISyntaxException
 * @throws JSONException
 * @throws ParseException
 */
public Map<String, String> getCycleIdByExecutions(int projectId, int versionId, String resource) throws URISyntaxException, JSONException, ParseException 
{
Map<String, String> executionIds = new HashMap<String, String>();

String createCycleUri = zephyrBaseUrl + resource;
 URI uri = new URI(createCycleUri);

HttpResponse response = null;
DefaultHttpClient restClient = new DefaultHttpClient();
HttpGet httpGet = new HttpGet(uri);
httpGet.setHeader("Authorization", getJwt(resource));
httpGet.setHeader("zapiAccessKey", this.apiKey);

try {
	response = restClient.execute(httpGet);
} catch (ClientProtocolException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}

int statusCode = response.getStatusLine().getStatusCode();

if (statusCode >= 200 && statusCode < 300) {
	HttpEntity entity1 = response.getEntity();
	String string1 = null;
	try {
		string1 = EntityUtils.toString(entity1);
	} catch (IOException e) {
		e.printStackTrace();
	}

	// System.out.println(string1);
	JSONObject allIssues = new JSONObject(string1);
	JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
	// System.out.println(IssuesArray.length());
	if (IssuesArray.length() == 0) {
		return executionIds;
	}
	for (int j = 0; j <= IssuesArray.length() - 1; j++) {
		JSONObject jobj = IssuesArray.getJSONObject(j);
		JSONObject jobj2 = jobj.getJSONObject("execution");
		String executionId = jobj2.getString("id");
		long IssueId = jobj2.getLong("issueId");
		executionIds.put(String.valueOf(IssueId), executionId);
	}
}
return executionIds;
}

/**
 * 
 * @param testNGTestCase
 * @return
 */
/*
public String getIssueKey(String testNGTestCase, int projectId)
{
	HashMap<String, String> myHash = new HashMap<String, String>();
	myHash = JIRATestCases.getTestCases(JIRAMetaData.getProjectName(projectId));
	return myHash.get(testNGTestCase);
}
*/

/**
 * 
 * @param projectId
 * @param versionId
 * @param resource
 * @param executeTestsJSON
 * @return
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 */
public String putExecutionsForCycle(int projectId, int versionId, String resource, StringEntity executeTestsJSON) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException
{
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();

	HttpPut executeTest = new HttpPut(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", putJwt(resource));
	executeTest.addHeader("zapiAccessKey", this.apiKey);
	executeTest.setEntity(executeTestsJSON);

	try {
		response = restClient.execute(executeTest);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();
	// System.out.println(statusCode);
	String executionStatus = "No Test Executed";
	// System.out.println(response.toString());
	HttpEntity entity = response.getEntity();

	if (statusCode >= 200 && statusCode < 300) {
		String string = null;
		try {
			string = EntityUtils.toString(entity);
			JSONObject executionResponseObj = new JSONObject(string);
			JSONObject descriptionResponseObj = executionResponseObj.getJSONObject("execution");
			JSONObject statusResponseObj = descriptionResponseObj.getJSONObject("status");
			executionStatus = statusResponseObj.getString("description");
			System.out.println(executionResponseObj.get("issueKey") + "--" + executionStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}

	} else {

		try {
			String string = null;
			string = EntityUtils.toString(entity);
			JSONObject executionResponseObj = new JSONObject(string);
			String cycleId = executionResponseObj.getString("clientMessage");
			// System.out.println(executionResponseObj.toString());
			throw new ClientProtocolException("Unexpected response status: " + statusCode);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}
	return executionStatus;
}

/**
 * 
 * @param TestCase
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String[]> getAllTestStepsForATestCase(String TestCase, String resource) throws URISyntaxException
{
	Map<String, String[]> executionIds = new HashMap<String, String[]>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("stepResults");
		if (IssuesArray.length() == 0) {
			return executionIds;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
			JSONObject jobj = IssuesArray.getJSONObject(j);
			String ExecutionData[] = new String[4];
			JSONObject jobj2 = jobj.getJSONObject("execution");
			String executionId = jobj2.getString("id");
			ExecutionData[0] = String.valueOf(jobj2.getLong("issueId"));
			ExecutionData[1] = jobj.getString("issueKey");
			ExecutionData[2] = jobj.getString("issueSummary");
			ExecutionData[3] = String.valueOf(jobj2.getJSONObject("status").getInt("id"));
			executionIds.put(executionId, ExecutionData);
		}
	}
	
	return executionIds;

}

/**
 * 
 * @param resource
 * @return
 * @throws URISyntaxException
 */
public Map<String, String> getStepResultsByExecution(String resource) throws URISyntaxException
{
	Map<String, String> myMappingList = new HashMap<String, String>();

	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
					
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("stepResults");
		if (IssuesArray.length() == 0) {
			return myMappingList;
		}
		
/**
 * Removed the Loop Index -1, not sure if there is a significance for the same.
 * Rachit Kumar Rastogi, rastogir, 04/23/2017 		
 */
//		for (int j = 0; j <= IssuesArray.length() - 1; j++) {
		for (int j = 0; j < IssuesArray.length(); j++) {	
		JSONObject jobj = IssuesArray.getJSONObject(j);
			myMappingList.put(jobj.getString("stepId"), jobj.getString("id"));
		}
	}
	
	return myMappingList;
}

/**
 * 
 * @param resource
 * @param executeTestsJSON
 * @return
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 */
public String putExecutionsForTestStep(String resource, StringEntity executeTestsJSON) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException
{
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();

	HttpPut executeTest = new HttpPut(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", putJwt(resource));
	executeTest.addHeader("zapiAccessKey", this.apiKey);
	executeTest.setEntity(executeTestsJSON);

	try 
	{
		response = restClient.execute(executeTest);
	} 
	catch (ClientProtocolException e) 
	{
		e.printStackTrace();
	} 
	catch (IOException e) 
	{
		e.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();
	// System.out.println(statusCode);
	String executionStatus = "No Test Executed";
	// System.out.println(response.toString());
	HttpEntity entity = response.getEntity();

	if (statusCode >= 200 && statusCode < 300) {
		String string = null;
		try {
			string = EntityUtils.toString(entity);
			JSONObject executionResponseObj = new JSONObject(string);
			//JSONObject descriptionResponseObj = executionResponseObj.getJSONObject("execution");
			//JSONObject statusResponseObj = descriptionResponseObj.getJSONObject("status");
			//executionStatus = statusResponseObj.getString("description");
			//System.out.println(executionResponseObj.get("issueKey") + "--" + executionStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}

	} else {

		try {
			String string = null;
			string = EntityUtils.toString(entity);
			//JSONObject executionResponseObj = new JSONObject(string);
			//String cycleId = executionResponseObj.getString("clientMessage");
			// System.out.println(executionResponseObj.toString());
			//throw new ClientProtocolException("Unexpected response status: " + statusCode);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}
	return executionStatus;
}

/**
 * 
 * @param resource
 * @return
 * @throws URISyntaxException
 */

public Map<String, String[]> getAllTeststeps(String resource) throws URISyntaxException
{
	Map<String, String[]> myMappingList = new HashMap<String, String[]>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONArray myJson = new JSONArray(string1);
		
		for (int j = 0; j <= myJson.length() - 1; j++) {
			JSONObject jobj = myJson.getJSONObject(j);
			String[] ExecutionId = new String[2];
			ExecutionId[0] = String.valueOf(jobj.getLong("issueId"));
			ExecutionId[1] = jobj.getString("step");
			myMappingList.put(jobj.getString("id"), ExecutionId);
		}
	}
	
	return myMappingList;
}

/**
 * 
 * @param projectId
 * @return
 * @throws URISyntaxException 
 */
public Map<String, String> getAllCycles(String resource) throws URISyntaxException 
{
	Map<String, String> myMappingList = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONArray myJson = new JSONArray(string1);
		
		for (int j = 0; j <= myJson.length() - 1; j++) {
			JSONObject jobj = myJson.getJSONObject(j);
			myMappingList.put(jobj.getString("name"), jobj.getString("id"));
		}
	}
	
	return myMappingList;
}

public List<List<String>> getJIRAExecutionsZephyrData(String resource, String cycleName) throws URISyntaxException
{
	List<List<String>> myJiraExecutionSummary = new ArrayList<List<String>>();
	List<String>  myJiraExecutionRecordHeader = new ArrayList<String>();
	
	/*
	myJiraExecutionRecordHeader.add("Project");
	myJiraExecutionRecordHeader.add("Test Cycle");
	myJiraExecutionRecordHeader.add("Test Case Key");
	myJiraExecutionRecordHeader.add("Test Case Description");
	myJiraExecutionRecordHeader.add("Test Status");
	myJiraExecutionRecordHeader.add("Test Executed By");
	myJiraExecutionRecordHeader.add("Test Executed On");

	myJiraExecutionSummary.add(myJiraExecutionRecordHeader);
	*/
	
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject allIssues = new JSONObject(string1);
		JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
		if (IssuesArray.length() == 0) {
			return myJiraExecutionSummary;
		}
		for (int j = 0; j <= IssuesArray.length() - 1; j++) {

				JSONObject jobj = IssuesArray.getJSONObject(j);
				// Initialize the Record with required information here.
				List<String>  myJiraExecutionRecord = new ArrayList<String>();
				JSONObject jobj2 = jobj.getJSONObject("execution");
				myJiraExecutionRecord.add(JIRAMetaData.getProjectName(jobj2.getInt("projectId")));
				//myJiraExecutionRecord.add(jobj2.getString("cycleId"));
				myJiraExecutionRecord.add(cycleName);
				myJiraExecutionRecord.add(jobj.getString("issueKey"));
				myJiraExecutionRecord.add(jobj.getString("issueSummary"));
				myJiraExecutionRecord.add(jobj2.getJSONObject("status").getString("name"));	
				try
				{
					if(jobj2.getString("executedBy") != null)
					{
					myJiraExecutionRecord.add(jobj2.getString("executedBy"));
					}
		
				if(String.valueOf(jobj2.getDouble("executedOn")) != null)
					{
					Instant utcInstant = new Date((long) jobj2.getDouble("executedOn")).toInstant();
					ZonedDateTime there = ZonedDateTime.ofInstant(utcInstant, ZoneId.of("UTC"));
					//System.out.println(utcInstant);
					LocalDateTime here = there.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
					//System.out.println(here);
					myJiraExecutionRecord.add(here.toString());
					}
				}
				catch(JSONException jse)
				{
				}
				
				myJiraExecutionSummary.add(myJiraExecutionRecord);

		}
	}
	
	return myJiraExecutionSummary;	
}

/**
 * 
 * @param projectId
 * @return
 * @throws URISyntaxException 
 */
public Map<String, String> getAllCyclesIdWithNameMapping(String resource) throws URISyntaxException 
{
	Map<String, String> myMappingList = new HashMap<String, String>();

	 String createCycleUri = zephyrBaseUrl + resource;
	 URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", getJwt(resource));
	httpGet.setHeader("zapiAccessKey", this.apiKey);

	try {
		response = restClient.execute(httpGet);
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();

	if (statusCode >= 200 && statusCode < 300) {
		HttpEntity entity1 = response.getEntity();
		String string1 = null;
		try {
			string1 = EntityUtils.toString(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONArray myJson = new JSONArray(string1);
		
		for (int j = 0; j <= myJson.length() - 1; j++) {
			JSONObject jobj = myJson.getJSONObject(j);
			myMappingList.put(jobj.getString("id"), jobj.getString("name"));
		}
	}
	
	return myMappingList;
}

/**
 * CREATE META DATA FOR TESTS IN JIRA - CAUTION: USE THIS SECTION ONLY IF YOU WOULD LIKE TO CREATE THE META DATA FOR TEST IN JIRA USING ZEPHYR ADD-ON 
 */

/**
 * Create Test Cycle
 * @param projectId
 * @param versionId
 * @param resource
 * @param executeTestsJSON
 * @return
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public String createTestCycle(String resource, StringEntity executeTestsJSON) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException, InterruptedException
{
	String cycleId = null;
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();

	HttpPost executeTest = new HttpPost(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", postJwt(resource));
	executeTest.addHeader("zapiAccessKey", this.apiKey);
	executeTest.setEntity(executeTestsJSON);

	try {
		response = restClient.execute(executeTest);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();
	HttpEntity entity = response.getEntity();

	if (statusCode >= 200 && statusCode < 300) {
		String string = null;
		try {
			string = EntityUtils.toString(entity);
			JSONObject executionResponseObj = new JSONObject(string);
			System.out.println(executionResponseObj.get("name")+" Test Cycle is created successfully in the Project.");
			cycleId = executionResponseObj.get("id").toString();
			return cycleId;
		} catch (IOException e) {
			e.printStackTrace();
		}

	} else {

		try {
			throw new ClientProtocolException("Unexpected response status: " + statusCode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}
return cycleId;
}

/**
 * Assign Test Case to a Cycle
 * @param projectId
 * @param versionId
 * @param resource
 * @param executeTestsJSON
 * @return
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public void assignTestCaseToACycle(String testCase, String resource, StringEntity executeTestsJSON) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException, InterruptedException
{
	/**
	 * This sleep is primarily for the case when we have to create the meta data back to back.
	 */
	Thread.sleep(10000);
	
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();

	HttpPost executeTest = new HttpPost(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", postJwt(resource));
	executeTest.addHeader("zapiAccessKey", this.apiKey);
	executeTest.setEntity(executeTestsJSON);

	try {
		response = restClient.execute(executeTest);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();
	HttpEntity entity = response.getEntity();

	if (statusCode >= 200 && statusCode < 300) {
		String string = null;
		try {
			string = EntityUtils.toString(entity);
			System.out.println("Test Case "+testCase+" is assigned successfully to the Test Cycle.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	} else {

		try {
			throw new ClientProtocolException("Unexpected response status: " + statusCode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Assign test steps to a test case
 * @param resource
 * @param executeTestsJSON
 * @return
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public void assignTestStepToTestCase(String resource, StringEntity executeTestsJSON) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException, InterruptedException
{
	/**
	 * This sleep is primarily for the case when we have to create the meta data back to back.
	 */
	//Thread.sleep(10000);               //==Given Below 
	
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpPost executeTest = new HttpPost(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", postJwt(resource));
	executeTest.addHeader("zapiAccessKey", this.apiKey);
	executeTest.setEntity(executeTestsJSON);

	try {
		response = restClient.execute(executeTest);
		Thread.sleep(10000);                              //=== Bad Hard Wait RS Code 
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	int statusCode = response.getStatusLine().getStatusCode();
	HttpEntity entity = response.getEntity();

	if (statusCode >= 200 && statusCode < 300) {
		String string = null;
		try {
			string = EntityUtils.toString(entity);
			System.out.println("Test Step is assigned successfully to the Test Case.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	} else {

		try {
			throw new ClientProtocolException("Unexpected response status: " + statusCode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}
}

public void addAttachment(int projectId, int versionId, String cycleId, String issueId,String entityId, String attachmentPath) throws URISyntaxException, ParseException, IOException, JSONException {
	
	final String API_ADD_ATTACHMENT = "{SERVER}/public/rest/api/1.0/attachment";
	final String userName = "msgtesting";
	final String comment = "MSGQAAutomationTeam";
	final String entityName = "execution";
	
	// Add Attachment to a testcase.
	// Initializes the URL data type with strURL created above
	String resource = 	"/public/rest/api/1.0/attachment?issueId=" + issueId
			+ "&versionId=" + versionId + "&entityName=" + entityName + "&cycleId=" + cycleId + "&entityId="
			+ entityId + "&projectId=" + projectId  + "&comment="+comment;
	
	String createCycleUri = zephyrBaseUrl + resource;
	URI uri = new URI(createCycleUri);
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpPost executeTest = new HttpPost(uri);
	File file = new File(attachmentPath);
	MultipartEntity entity = new MultipartEntity();
	entity.addPart("attachment", new FileBody(file));

	HttpPost addAttachmentReq = new HttpPost(uri);
	addAttachmentReq.addHeader("Authorization", postJwt(resource));
	addAttachmentReq.addHeader("zapiAccessKey", this.apiKey);
	addAttachmentReq.setEntity(entity);

	try {
		response = restClient.execute(addAttachmentReq);
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	HttpEntity entity1 = response.getEntity();
	int statusCode = response.getStatusLine().getStatusCode();
	// System.out.println(statusCode);
	// System.out.println(response.toString());
	if (statusCode >= 200 && statusCode < 300) {
		System.out.println("Attachment added Successfully");
	} else {
		try {
			String string = null;
			string = EntityUtils.toString(entity1);
			System.out.println(string);
			throw new ClientProtocolException("Unexpected response status: " + statusCode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}

}

}
