package Utility.JIRA;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class for JIRA REST API
 * @author Rachit Kumar Rastogi
 * @version 1.0
 */

@SuppressWarnings("deprecation")
public class JIRAUtilities {
	
	/**
	 * Update the default parameters here.
	 */
	static final String jiraBaseUrl = "https://thegarden.atlassian.net";
	static final int defaultProjectId = JIRAMetaData.getProjectId("Rockettes");
	static final String defaultComponentId = JIRAMetaData.getComponentId("Rockettes");
	static final String defaultBasicAuthKey = "Basic bXNndGVzdGluZzpNc2dRYVRlYW0wMDck";
	
	int projectId;
	String basicAuthKey, secretKey, userName, componentId;
	
/**
 * Default Constructor
 */
	
public JIRAUtilities()
{
	this.basicAuthKey = defaultBasicAuthKey;
	this.projectId = defaultProjectId;
	this.componentId = defaultComponentId;
}

/**
 * Overloaded Constructor
 * @param basicAuthKey
 */
public JIRAUtilities(String basicAuthKey, int projectId, String componentId)
{
	this.basicAuthKey = basicAuthKey;
	this.projectId = projectId;
	this.componentId = componentId;
}

/**
 * Overloaded Constructor
 * @param basicAuthKey
 * @param projectId
 * @param componentId
 */
public JIRAUtilities(int projectId, String componentId)
{
	this.basicAuthKey = defaultBasicAuthKey;
	this.projectId = projectId;
	this.componentId = componentId;
}

/**
 * Overloaded Constructor
 * @param basicAuthKey
 * @param projectId
 * @param componentId
 */
public JIRAUtilities(String projectId)
{
	this.basicAuthKey = defaultBasicAuthKey;
	this.projectId = JIRAMetaData.getProjectId(projectId);
	this.componentId = JIRAMetaData.getComponentId(projectId);
}

/**
 * Get Issue meta data
 * @param resource
 * @throws URISyntaxException
 */
public void getIssueMetaData(String resource) throws URISyntaxException
{
	 String getIssueURL = jiraBaseUrl + resource;
	 URI uri = new URI(getIssueURL);
	
	//Example URI: https://thegarden.atlassian.net/rest/api/2/issue/MSGEW-1700/");
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

	try {
		response = restClient.execute(httpGet);
		System.out.println(response);
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
			System.out.println(string1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Get Outward Issues of a Issue in JIRA.
 * @param resource
 * @throws URISyntaxException
 */
@SuppressWarnings("null")
public List<String> getIssueOutwardIssues(String IssueKey) throws URISyntaxException
{
	 List<String> linkedOutwardIssues = new ArrayList<String>();
	 String getIssueURL = jiraBaseUrl + "/rest/api/latest/issue/" + IssueKey;
	 URI uri = new URI(getIssueURL);
	
	//Example URI: https://thegarden.atlassian.net/rest/api/2/issue/MSGEW-1700/");
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

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

		JSONObject IssueMetaData = new JSONObject(string1);
		JSONObject IssuefieldData = IssueMetaData.getJSONObject("fields");
		JSONArray IssueLinksData = IssuefieldData.getJSONArray("issuelinks");
		if (IssueLinksData.length() == 0) {
			return null;
		}		

		for (int j = 0; j <= IssueLinksData.length() - 1; j++) {
			JSONObject IssueLinked = IssueLinksData.getJSONObject(j);
			
			for(Object Keys: IssueLinked.keySet())
			{
			if(Keys.equals("outwardIssue"))
				{
				JSONObject IssueKeyObject = IssueLinked.getJSONObject("outwardIssue");
				for(Object IssuesKeys: IssueKeyObject.keySet())
					{
					if (IssuesKeys.equals("key"))
						{
							//System.out.println(IssueKeyObject.getString("key"));
							linkedOutwardIssues.add(IssueKeyObject.getString("key"));
						}
					}
				}
			}
		}
	}
	
	return linkedOutwardIssues;
}

/**
 * Get Related Issues of a Issue in JIRA.
 * @param resource
 * @throws URISyntaxException
 */
@SuppressWarnings("null")
public List<String> getIssueRelatedIssues(String IssueKey) throws URISyntaxException
{
	 List<String> linkedOutwardIssues = new ArrayList<String>();
	 String myInwardIssueKey = null;
	 String myOutwardIssueKey = null;
	 String getIssueURL = jiraBaseUrl + "/rest/api/latest/issue/" + IssueKey;
	 URI uri = new URI(getIssueURL);
	
	//Example URI: https://thegarden.atlassian.net/rest/api/2/issue/MSGEW-1700/");
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

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

		JSONObject IssueMetaData = new JSONObject(string1);
		JSONObject IssuefieldData = IssueMetaData.getJSONObject("fields");
		JSONArray IssueLinksData = IssuefieldData.getJSONArray("issuelinks");
		if (IssueLinksData.length() == 0) {
			return null;
		}		

		for (int j = 0; j <= IssueLinksData.length() - 1; j++) {
			Boolean isRelatedIssuesPresent = false;
			myOutwardIssueKey = null;
			myInwardIssueKey = null;
			
			JSONObject IssueLinked = IssueLinksData.getJSONObject(j);
			
			for(Object Keys: IssueLinked.keySet())
			{
			if(Keys.equals("type"))
			{
				JSONObject IssueKeyObject = IssueLinked.getJSONObject("type");
				for(Object IssuesKeys: IssueKeyObject.keySet())
					{
					if (IssuesKeys.equals("name"))
						{
							if(IssueKeyObject.getString("name").equals("Relates"))
									{
										isRelatedIssuesPresent = true;
									}
						}
					}
			}
				
			if(Keys.equals("outwardIssue"))
				{
				JSONObject IssueKeyObject = IssueLinked.getJSONObject("outwardIssue");
				for(Object IssuesKeys: IssueKeyObject.keySet())
					{
					if (IssuesKeys.equals("key"))
						{
							myOutwardIssueKey = IssueKeyObject.getString("key");
						}
					}
				}
			
			if(Keys.equals("inwardIssue"))
				{
			JSONObject IssueKeyObject = IssueLinked.getJSONObject("inwardIssue");
			for(Object IssuesKeys: IssueKeyObject.keySet())
					{
				if (IssuesKeys.equals("key"))
						{
							myInwardIssueKey = IssueKeyObject.getString("key");
						}
					}
				}
			
			if(isRelatedIssuesPresent)
			{
				if (myInwardIssueKey !=null)
					{
						linkedOutwardIssues.add(myInwardIssueKey);	
					}
				if (myOutwardIssueKey !=null)
					{
						linkedOutwardIssues.add(myOutwardIssueKey);
					}
			}
			
			
			}
		}
	}
	
	return linkedOutwardIssues;
}

/**
 * Get the User Assigned to the User Story in JIRA.
 * @param IssueKey
 * @return
 * @throws URISyntaxException
 */
public String getUserAssignedToStoryInJira(String IssueKey) throws URISyntaxException
{
	String getIssueURL = jiraBaseUrl + "/rest/api/latest/issue/" + IssueKey;
	URI uri = new URI(getIssueURL);
	
	//Example URI: https://thegarden.atlassian.net/rest/api/2/issue/MSGEW-1700/");
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

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

		JSONObject IssueMetaData = new JSONObject(string1);
		JSONObject IssueData = IssueMetaData.getJSONObject("fields");
		JSONObject IssueAssigneeData = IssueData.getJSONObject("assignee");
		return IssueAssigneeData.getString("key");
	}
	return null;
}

/**
 * Create Bug Card for Corresponding User.
 * @param projectId
 * @param componentId
 * @param userName
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 */
public String createIssueInJira(int projectId, String componentId, String bugToBeAssignedToUser, String bugDescription) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException
{
	String getIssueURL = jiraBaseUrl + "/rest/api/2/issue";
	URI uri = new URI(getIssueURL);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	
	JSONObject executeTestsObjParent = new JSONObject();
	JSONObject executeTestsObj = new JSONObject();
	
	JSONObject project = new JSONObject();
	project.put("id", projectId);
	executeTestsObj.put("project", project);	
	
	executeTestsObj.put("summary", bugDescription);
	
	JSONObject issueType = new JSONObject();
	issueType.put("id", "1");
	executeTestsObj.put("issuetype", issueType);	
	
	JSONObject assignee = new JSONObject();
	assignee.put("name", bugToBeAssignedToUser);
	executeTestsObj.put("assignee", assignee);	
	
	// Commented by: Rachit Kumar Rastogi, Date: 06/26/2017, 4.44PM at Huge Inc.
	// Commented as Reporter is not a required field for MDT project.
	// - However it's a short term solution and shall ideally be addressed in the project customization settings. 
	/*
	JSONObject reporter = new JSONObject();
	reporter.put("name", "msgtesting");
	executeTestsObj.put("reporter", reporter);	
    */
	
	JSONObject priority = new JSONObject();
	priority.put("id", "3");
	executeTestsObj.put("priority", priority);	
	
	if (!componentId.isEmpty() && componentId!=null)
	{
		JSONArray  componentsArray = new JSONArray();
		JSONObject components = new JSONObject();
		components.put("id", componentId);
		componentsArray.put(components);
		executeTestsObj.put("components", componentsArray);	
	}
	
	executeTestsObjParent.put("fields", executeTestsObj);

	StringEntity executeTestsJSON = null;
	
	try {
		executeTestsJSON = new StringEntity(executeTestsObjParent.toString());
		} 
	catch (UnsupportedEncodingException e1) 
		{
		e1.printStackTrace();
		}
	
	HttpPost executeTest = new HttpPost(uri);
	//HttpPut executeTest = new HttpPut(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", this.basicAuthKey);
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
			System.out.println(executionResponseObj.get("key") + " -- Bug Card Created Successfully.");
			return executionResponseObj.get("key").toString();
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
	return "Error: Bug card is not created.";
}

/**
 * Link the Bug Card to the User Story
 * @param inwardIssue
 * @param outwardIssue
 * @throws IOException 
 * @throws org.apache.http.ParseException 
 * @throws URISyntaxException 
 */
public void linkIssueWithUserStoryInJira(String inwardIssue, String outwardIssue) throws org.apache.http.ParseException, IOException, URISyntaxException
{
	String getIssueURL = jiraBaseUrl + "/rest/api/2/issueLink/";
	URI uri = new URI(getIssueURL);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	
	JSONObject executeTestsObj = new JSONObject();
	
	JSONObject linkType = new JSONObject();
	linkType.put("name", "Relates");
	executeTestsObj.put("type", linkType);	
	
	JSONObject issueType = new JSONObject();
	issueType.put("key", inwardIssue);
	executeTestsObj.put("inwardIssue", issueType);	
	
	JSONObject assignee = new JSONObject();
	assignee.put("key", outwardIssue);
	executeTestsObj.put("outwardIssue", assignee);	
	
	StringEntity executeTestsJSON = null;
	
	try {
		executeTestsJSON = new StringEntity(executeTestsObj.toString());
		} 
	catch (UnsupportedEncodingException e1) 
		{
		e1.printStackTrace();
		}
	
	HttpPost executeTest = new HttpPost(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", this.basicAuthKey);
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

	if (statusCode !=400) 
		{
		System.out.println(inwardIssue+" -- Card Linked Successfully with User Story -- "+outwardIssue+".");
		}
}

/**
 * Method for End to End work-flow of creating Bug and assigning to the person assigned in User Story.
 * @param TestCaseKey
 * @param ConsolidatedTestResults
 * @param isAllTestResulInOneBugCard
 * @throws URISyntaxException
 * @throws org.apache.http.ParseException
 * @throws ParseException
 * @throws IOException
 */
public void createBugWithUserAssignmentInJira(String TestCaseKey, String ConsolidatedTestResults, boolean isAllTestResulInOneBugCard, String bugDescription) throws URISyntaxException, org.apache.http.ParseException, ParseException, IOException
{
	//1. Find the Corresponding User Story for TestCaseKey (JIRA REST Call).
	List<String> myRelatedIssues = getIssueRelatedIssues(TestCaseKey);
	
	if(myRelatedIssues.size() > 0)
	{
		for(int count=0; count<myRelatedIssues.size(); count++)
		{
			//2. Find the User Assigned to the User Story.
			String assigneeUser = getUserAssignedToStoryInJira(myRelatedIssues.get(count));
			
			//3. Create Bug Card for Corresponding User & Link the Bug Card to the User Story.
			linkIssueWithUserStoryInJira(createIssueInJira(projectId, componentId, assigneeUser, bugDescription), myRelatedIssues.get(count));
		}
	}
	
	else
	{
		System.out.println("No User Story is mapped to the test therefore a indepedent Bug card will be created.");

		//2. Create the Bug Card.
		createIssueInJira(projectId, componentId, "msgtesting", bugDescription);
	}
}


/**
 * Create Issue type Test in JIRA
 * @param projectId
 * @param componentId
 * @param userName
 * @throws URISyntaxException
 * @throws ParseException
 * @throws org.apache.http.ParseException
 * @throws IOException
 */
public String createTestInJira(int projectId, String componentId, String bugToBeAssignedToUser, String bugDescription) throws URISyntaxException, ParseException, org.apache.http.ParseException, IOException
{
	String getIssueURL = jiraBaseUrl + "/rest/api/2/issue";
	URI uri = new URI(getIssueURL);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	
	JSONObject executeTestsObjParent = new JSONObject();
	JSONObject executeTestsObj = new JSONObject();
	
	JSONObject project = new JSONObject();
	project.put("id", projectId);
	executeTestsObj.put("project", project);	
	
	executeTestsObj.put("summary", bugDescription);
	
	// Please note that 10500 is Issue Type TEST in JIRA.
	JSONObject issueType = new JSONObject();
	issueType.put("id", "10500");
	executeTestsObj.put("issuetype", issueType);	
	
	JSONObject assignee = new JSONObject();
	assignee.put("name", bugToBeAssignedToUser);
	executeTestsObj.put("assignee", assignee);	
	
	/* some Projects doesn't have a default reporter in theie create issue screen e.g. Event Engine so this is giving dump.
	JSONObject reporter = new JSONObject();
	reporter.put("name", "msgtesting");
	executeTestsObj.put("reporter", reporter);	
	 */
	
	JSONObject priority = new JSONObject();
	priority.put("id", "3");
	executeTestsObj.put("priority", priority);	
	
	if (!componentId.isEmpty() && componentId!=null)
	{
		JSONArray  componentsArray = new JSONArray();
		JSONObject components = new JSONObject();
		components.put("id", componentId);
		componentsArray.put(components);
		executeTestsObj.put("components", componentsArray);	
	}
	
	executeTestsObjParent.put("fields", executeTestsObj);

	StringEntity executeTestsJSON = null;
	
	try {
		executeTestsJSON = new StringEntity(executeTestsObjParent.toString());
		} 
	catch (UnsupportedEncodingException e1) 
		{
		e1.printStackTrace();
		}
	
	HttpPost executeTest = new HttpPost(uri);
	//HttpPut executeTest = new HttpPut(uri);
	executeTest.addHeader("Content-Type", "application/json");
	executeTest.addHeader("Authorization", this.basicAuthKey);
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
			System.out.println(executionResponseObj.get("key") + " -- Test Created Successfully.");
			return executionResponseObj.get("key").toString();
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
	return "Error: Test is not created.";
}

/**
 * Automation Scenario: Create Test Cases Automatically for the User Stories in current Sprint
 * Use below method to do following:
 * 1. Retrieve the Board ID of a Project (e.g. Rockettes Project in JIRA will have a Board ID)
 * 2. Once Board ID is obtained then get the Active Sprint Id
 * 3. Once Sprint Id is obtained then get the issues assigned to the Sprint
 * 4. Now Create Test Cycle under the same Project with the name as <SprintName>TestCycle and validity of Sprint
 * 5. Once Test Cycle is created then create tests for all user stories
 * 6. Link the Tests with User Stories 
 * 
 * @return
 * @throws URISyntaxException 
 */

/**
 * 1. Get the Board ID.
 * @return
 * @throws URISyntaxException
 */
public HashMap<String, String> getAllBoardNameAndIdsForAProject() throws URISyntaxException
{
	HashMap<String, String> myProjectBoards = new HashMap<String, String>();
	
	 String getIssueURL = jiraBaseUrl + "/rest/agile/1.0/board/";
	 URI uri = new URI(getIssueURL);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

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
		
		JSONObject myJsonObj = new JSONObject(string1);	
		JSONArray myJson = myJsonObj.getJSONArray("values");
	
	for (int j = 0; j < myJson.length(); j++) {
		JSONObject jobj = myJson.getJSONObject(j);
		myProjectBoards.put(jobj.getString("name"), String.valueOf(jobj.getLong("id")));
	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return myProjectBoards;
}

/**
 * 1. Get the 'ACTIVE' Sprint ID.
 * @return
 * @throws URISyntaxException
 */
public HashMap<String, String[]> getActiveSprintNameAndIdsForABoard(String boardId) throws URISyntaxException
{
	HashMap<String, String[]> myActiveSprintDetails = new HashMap<String, String[]>();
	
	 String getIssueURL = jiraBaseUrl + "/rest/agile/1.0/board/"+boardId+"/sprint?state=active";
	 URI uri = new URI(getIssueURL);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

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
		
		JSONObject myJsonObj = new JSONObject(string1);	
		JSONArray myJson = myJsonObj.getJSONArray("values");
	
	for (int j = 0; j < myJson.length(); j++) {
		JSONObject jobj = myJson.getJSONObject(j);
		String[] mySprintDetails = new String[4];
		mySprintDetails[0] = String.valueOf(jobj.get("id"));
		mySprintDetails[1] = jobj.getString("name");
		//mySprintDetails[2] = jobj.getString("startDate").substring(0, 10);
		//mySprintDetails[3] = jobj.getString("endDate").substring(0, 10);
		mySprintDetails[2] = jobj.getString("startDate").substring(0,10)+" "+jobj.getString("startDate").substring(11,23);
		mySprintDetails[3] = jobj.getString("endDate").substring(0,10)+" "+jobj.getString("endDate").substring(11,23);
		myActiveSprintDetails.put(boardId, mySprintDetails);
	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return myActiveSprintDetails;
}

/**
 * 1. Get the issues in Sprint
 * @return
 * @throws URISyntaxException
 */
public HashMap<String, String[]> getAllIssuesInActiveSprintOfABoard(String boardId, String sprintId, boolean skipCompletedUserStories) throws URISyntaxException
{
	HashMap<String, String[]> myIssues = new HashMap<String, String[]>();
	
	 String getIssueURL = jiraBaseUrl + "/rest/agile/1.0/board/"+boardId+"/sprint/"+sprintId+"/issue";
	 URI uri = new URI(getIssueURL);
	
	HttpResponse response = null;
	DefaultHttpClient restClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(uri);
	httpGet.setHeader("Authorization", this.basicAuthKey);

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
		
		JSONObject myJsonObj = new JSONObject(string1);	
		JSONArray myJson = myJsonObj.getJSONArray("issues");
	
	for (int j = 0; j < myJson.length(); j++) {
		JSONObject jobj = myJson.getJSONObject(j);
		String[] myIssueDetails = new String[3];
		myIssueDetails[0] = String.valueOf(jobj.get("id"));
		myIssueDetails[1] = jobj.getString("key");
		JSONObject myIssueFields = jobj.getJSONObject("fields");
		String myIssueSummary = myIssueFields.getString("summary");
		JSONObject myIssueStatusMatrix = myIssueFields.getJSONObject("status");
		String myIssueStatus = myIssueStatusMatrix.getString("name");
		myIssueDetails[2] = myIssueStatusMatrix.getString("name");
		if (!skipCompletedUserStories)
		{
			myIssues.put(myIssueSummary, myIssueDetails);	
		}
		
		else
		{
			 if (!myIssueStatus.equals("Complete"))
			 {
				 myIssues.put(myIssueSummary, myIssueDetails);	
			 }
		}
	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	return myIssues;
}


/**
 * Automation Scenario: Add the Attachment for automation results.
 * 1. Build the attachment e.g. type of it: doc, html, txt etc
 * 2. Add the attachment to the Test Step (ZEPHYR)
 * 3. Add the attachment to the Bug Card (JIRA)
 */



}