package Utility.JIRA;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.UniformInterfaceException;

public class MSGJIRAupdate {
	
	int projectId;
	int versionId;
	static String cycleId;
	String cycleName;
	String componentId;
	
	public JIRAUtilities jiraUtilities;
	
	/**
	 * Overloaded Constructor with parameters for Project, Version & Cycle
	 * @param projectId
	 * @param versionId
	 * @param cycleId
	 */
public	MSGJIRAupdate(int projectId, int versionId, String cycleId)
	{
		this.projectId = projectId;
		this.versionId = versionId;
		MSGJIRAupdate.cycleId = cycleId;
		this.jiraUtilities = new JIRAUtilities(projectId,"");
	}

/**
 * Overloaded Constructor for Alphanumeric Project/cycleId
 * @param projectId
 * @param versionId
 * @param cycleId
 */
public	MSGJIRAupdate(String projectId, int versionId, String cycleId)
	{	
		this.projectId = JIRAMetaData.getProjectId(projectId);
		this.versionId = versionId;
		MSGJIRAupdate.cycleId = getcycleId(cycleId, this.projectId, this.versionId);
		this.cycleName = cycleId;
		this.componentId = JIRAMetaData.getComponentId(projectId);
		this.jiraUtilities = new JIRAUtilities(this.projectId,this.componentId);
	}

/**
 *  Overloaded Constructor for JIRA REST update
 * @param projectId
 * @param versionId
 * @param cycleId
 * @param jiraUtilities
 */
public	MSGJIRAupdate(int projectId, int versionId, String cycleId, JIRAUtilities jiraUtilities)
{
	this.projectId = projectId;
	this.versionId = versionId;
	MSGJIRAupdate.cycleId = cycleId;
	this.jiraUtilities = jiraUtilities;
}

/**
 * Overloaded Constructor for JIRA REST update with Alphanumeric Project
 * @param projectId
 * @param versionId
 * @param cycleId
 */
public	MSGJIRAupdate(String projectId, int versionId, String cycleId, JIRAUtilities jiraUtilities)
	{	
		this.projectId = JIRAMetaData.getProjectId(projectId);
		this.versionId = versionId;
		MSGJIRAupdate.cycleId = getcycleId(cycleId, this.projectId, this.versionId);
		this.jiraUtilities = jiraUtilities;
	}
	
/**
 * Main Method for JIRA Update
 * @param args
 * @throws UniformInterfaceException
 * @throws URISyntaxException
 * @throws ParseException
 * @throws java.text.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public static void main(String args[]) throws UniformInterfaceException, URISyntaxException, ParseException, java.text.ParseException, IOException, InterruptedException
	{
		/**
		 * 1. Get the Zephyr Instance for respective cycle in the Project.
		 */
		//MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("MSGAutomation",11200, "SmokeTesting");
		//MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("Rockettes",11500, "Ad hoc");		
		//MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("Rockettes",11500, "SmokeTesting");	
		//MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("Rockettes",11500, "My Test Cycle");
		//MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("EventEngine",-1, "My Test Cycle");
	
		//MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("SalesCenter",-1, "SmokeTesting");
	MSGJIRAupdate myMSGJiraUpdater = new MSGJIRAupdate("SalesCenter",-1, "FullRegression");	
	System.out.println(myMSGJiraUpdater.getAllIssueSummaryAndKeysAssignedToACycle()); 
		
		/**
		 * 2. Update the Status of a Single Test Case && Create a Bug Too (Isn't it awesome?)
		 */
		//myMSGJiraUpdater.updateStatusForSingleTestCasesInJira("MSGRock008RockettesCSExperiences", "PASS");	
		//myMSGJiraUpdater.updateStatusForSingleTestCasesInJira("TC01", "FAIL");
	
		/**
		 * 3. Update the status of All Test Steps of SINGLE Test Case
		 */
		//String testCaseName = "MSGRock008RockettesCSExperiences", testStepParameters = "iOS iPhone 7 Plus", testStepStatus = "PASS";
		//myMSGJiraUpdater.updateStatusForSingleTestStepsInJira(testCaseName, testStepParameters,testStepStatus );
		
		//testCaseName = "MSGRock008RockettesCSExperiences"; 
		//testStepParameters = "MAC Safari 10";
		//testStepStatus = "PASS";
		//myMSGJiraUpdater.updateStatusForSingleTestStepsInJira(testCaseName, testStepParameters,testStepStatus );
		 
		/**
		 * 4. Clear Status of all Test Cases and Steps.
		 */
		//myMSGJiraUpdater.clearStatusForAllTestCasesInJira();
		 
		 /**
		  * 5. Get reporting data.
		  */
		//myMSGJiraUpdater.generateHTMLJIRAReport();
		
		/**
		 * 6. Create Meta data, hang-on look at the data in JIRA do we've that cycle existing, kidding we'll check it for you.
		 */
		
		// Test Cycle
		//cycleId = myMSGJiraUpdater.createTestCycleInJira("SmokeTesting");
	cycleId = myMSGJiraUpdater.createTestCycleInJira("FullRegression");

		/*// Test Case
		String myTestCaseIssueId = myMSGJiraUpdater.createTestCaseInJira("My Test Case");
	
		// Test Step
		myMSGJiraUpdater.createTestStepInJira(myTestCaseIssueId, "My Test Case","My Test Step3", "My Test Step data", "My Test Step Result");

		// Update the Status of Test Case and Test Step
	    myMSGJiraUpdater.updateStatusForSingleTestCasesInJira("My Test Case", "PASS");	
			
		myMSGJiraUpdater.updateStatusForSingleTestStepsInJira("My Test Case", "My Test Step3s", "PASS");	
		
		*//**
		 * 7. Create Meta Data, buddy this one is really cool: We are going to read the Board (Rockettes) -> Find active Sprint
		 *  -> find User stories assigned to the Sprint -> Create Tests for those user stories -> assign tests to user stories
		 *//*
		
		//myMSGJiraUpdater.createAutomaticTestCasesUsingSprintDataInAProject("Rockettes Board");
		//myMSGJiraUpdater.createAutomaticTestCasesUsingSprintDataInAProject("Event Engine");
		
		*//**
		 * 8. Create an Attachment in JIRA Test case.
		 *//*
		String attachmentPath = "/Users/rastogir/git/MSGSalesCenterAutomatedTesting/SalesCenter/reports/Fri_Jun_02_18_00_38_EDT_2017.html", issueKey = "MDT-655";
		myMSGJiraUpdater.createAnAttachmentInJira(issueKey, attachmentPath);
		*/
	}

/**
 * * This Section Covers the Methods Related to the ZEPHYR Add-on of JIRA.
 */
	
	/**
	 * Update Status for single Test Case in Jira.
	 * @param TestCase
	 * @param Status
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @throws java.text.ParseException
	 * @throws ParseException
	 * @throws IOException
	 */
public void updateStatusForSingleTestCasesInJira(String TestCase, String Status) throws JSONException, URISyntaxException, java.text.ParseException, ParseException, IOException
	{
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		
		/**
		 * 1. Fetch Test Cases for Rockettes - SmokeTesting Cycle
		 */
		Map<String, String> myExecutionIds = myzephyr.getExecutionsByCycleId(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		
		/**
		 * 2. Let's Update the Status of Test Cases
		 * a. Fetch the HashMap for Test and Ticket Mapping.
		 * b. Fetch the HashMap for Issue Key (Ticket) and Issue Id mapping.
		 * c. From a & b above get the Result (Pass/Fail) for Test and put it in the status below. 
		 */
	
		//HashMap<String, String> myTestCases = JIRATestCases.getTestCases(JIRAMetaData.getProjectName(projectId));
		
		Map<String, String> myTestCases = getAllIssueSummaryAndKeysAssignedToACycle();
	
		String myIssueKey = null;
		
		for(String key: myTestCases.keySet())
		{
			if(key.contains(TestCase))
			{
				myIssueKey = myTestCases.get(key);
				break;
			}
		}
		
		Map<String, String> myIssueKeyIdMapping = myzephyr.getIssueKeyIdMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		
		JSONObject executeTestsObj = new JSONObject();
		
		executeTestsObj.put("cycleId", MSGJIRAupdate.cycleId);
		executeTestsObj.put("projectId", projectId);
		executeTestsObj.put("versionId", versionId);
		executeTestsObj.put("comment", "Executed by MSG QA Automation Team.");
		
		if(Status == "PASS")
		{
			JSONObject statusObj = new JSONObject();
			statusObj.put("id", "1");
			executeTestsObj.put("status", statusObj);	
		}
		
		if(Status == "FAIL")
		{
			JSONObject statusObj = new JSONObject();
			statusObj.put("id", "2");
			executeTestsObj.put("status", statusObj);	
			
			// Create a Bug Card in JIRA for failed scenarios by utilizing the JIRA REST Capabilities.
			//jiraUtilities.createBugWithUserAssignmentInJira(myIssueKey, "", false, "Bug Card for: "+TestCase+" Test Case.");
		}
		
		String myIssueId = myIssueKeyIdMapping.get(myIssueKey);
		executeTestsObj.put("issueId", myIssueId);
		StringEntity executeTestsJSON = null;
		try {
			executeTestsJSON = new StringEntity(executeTestsObj.toString());
			} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		for (String key : myExecutionIds.keySet()) 
		{
			if (myExecutionIds.get(key).equals(myIssueId))
			{
				myzephyr.putExecutionsForCycle(11001, 11500, "/public/rest/api/1.0/execution/"+key, executeTestsJSON);
			}
		}
	}
	
	/**
	 * Update Status for single test step in Jira.
	 * @param testCaseName
	 * @param testStepPlatform
	 * @param testStepStatus
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @throws java.text.ParseException
	 * @throws ParseException
	 * @throws IOException
	 */
public void updateStatusForSingleTestStepsInJira(String testCaseName, String testStepPlatform, String testStepStatus) throws JSONException, URISyntaxException, java.text.ParseException, ParseException, IOException
	{
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		
		/**
		 * 1. Fetch Issue id
		 */
		Map<String, String> myIssueIDs = myzephyr.getCycleIdByExecutions(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		
		/**
		 * 2. Let's Update the Status of Test Cases
		 * a. Fetch the HashMap for Test and Ticket Mapping.
		 * b. Fetch the HashMap for Issue Key (Ticket) and Issue Id mapping.
		 * c. From a & b above get the Result (Pass/Fail) for Test and put it in the status below. 
		 */		
		Map<String, String> myTestCases = getAllIssueSummaryAndKeysAssignedToACycle();
		String myIssueKey = null;
		
		for(String key: myTestCases.keySet())
		{
			if(key.contains(testCaseName))
			{
				myIssueKey = myTestCases.get(key);
				break;
			}
		}
		
		Map<String, String> myIssueKeyIdMapping = myzephyr.getIssueKeyIdMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		String myIssueId = myIssueKeyIdMapping.get(myIssueKey);
		String myExecutionId = myIssueIDs.get(myIssueId);
	
		Map<String, String[]> myTestStepsMetadata = myzephyr.getAllTeststeps("/public/rest/api/1.0/teststep/"+myIssueId+"?projectId="+projectId);

		Map<String, String> myTestSteps = myzephyr.getStepResultsByExecution("/public/rest/api/1.0/stepresult/search?executionId="+myExecutionId+"&issueId="+myIssueId+"&isOrdered=true");
		
		for (String key : myTestStepsMetadata.keySet()) 
		{
			String[] IdAndStepId = new String[2];
			
			IdAndStepId = myTestStepsMetadata.get(key);
					
			if(IdAndStepId[1].equals(testStepPlatform))
			{
				JSONObject executeTestsObj = new JSONObject();
				
				executeTestsObj.put("issueId", myIssueId);
				executeTestsObj.put("stepId", key);
				executeTestsObj.put("executionId",myExecutionId) ;
				executeTestsObj.put("comment", "Executed by MSG QA Automation Team.");
				
				if(testStepStatus == "PASS")
				{
					JSONObject statusObj = new JSONObject();
					statusObj.put("id", "1");
					executeTestsObj.put("status", statusObj);	
				}
				
				if(testStepStatus == "FAIL")
				{
					JSONObject statusObj = new JSONObject();
					statusObj.put("id", "2");
					executeTestsObj.put("status", statusObj);	
				}
				
				StringEntity executeTestsJSON = null;
				try {
					executeTestsJSON = new StringEntity(executeTestsObj.toString());
					} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				myzephyr.putExecutionsForTestStep("/public/rest/api/1.0/stepresult/"+myTestSteps.get(key), executeTestsJSON);
			}
		}
	}	
	
	/**
	 * !!! Caution: Clear Status for all Test Cases in Jira. !!!
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @throws java.text.ParseException
	 * @throws ParseException
	 * @throws IOException
	 */
public void clearStatusForAllTestCasesInJira() throws JSONException, URISyntaxException, java.text.ParseException, ParseException, IOException
	{
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		
		/**
		 * 1. Fetch Test Cases for Rockettes - SmokeTesting Cycle
		 */
		Map<String, String> myExecutionIds = myzephyr.getExecutionsByCycleId(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		
		/**
		 * 2. Let's Update the Status of Test Cases
		 * a. Fetch the HashMap for Test and Results (Pass/Fail). 
		 * b. Fetch the HashMap for Test and Ticket Mapping.
		 * c. Fetch the HashMap for Issue Key (Ticket) and Issue Id mapping.
		 * d. From a, b & c above get the Result (Pass/Fail) for Test and put it in the status below. 
		 */
		
		JSONObject executeTestsObj = new JSONObject();
		
		executeTestsObj.put("cycleId", cycleId);
		executeTestsObj.put("projectId", projectId);
		executeTestsObj.put("versionId", versionId);
		executeTestsObj.put("comment", "Executed by MSG QA Automation Team.");
		
		for (String key : myExecutionIds.keySet()) {
		JSONObject statusObj = new JSONObject();
		statusObj.put("id", "-1");
		executeTestsObj.put("status", statusObj);	
		executeTestsObj.put("issueId", myExecutionIds.get(key));
		StringEntity executeTestsJSON = null;
		try {
			executeTestsJSON = new StringEntity(executeTestsObj.toString());
			} 
		catch (UnsupportedEncodingException e1) 
			{
			e1.printStackTrace();
			}
		myzephyr.putExecutionsForCycle(projectId, versionId,  "/public/rest/api/1.0/execution/"+key, executeTestsJSON);
		}
		/**
		 * 3. Clear status for all test Steps.
		 */
		System.out.println("Clearing the Status of all Test steps in the Test Case....");
		clearStatusForAllTestStepsInJira();
	}	
	
	/**
	 * Update Status for All test step in Jira.
	 * @param testCaseName
	 * @param testStepPlatform
	 * @param testStepStatus
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @throws java.text.ParseException
	 * @throws ParseException
	 * @throws IOException
	 */
public void clearStatusForAllTestStepsInJira() throws JSONException, URISyntaxException, java.text.ParseException, ParseException, IOException
	{
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		
		/**
		 * 1. Fetch Test Cases for Rockettes - SmokeTesting Cycle
		 */
		Map<String, String> myIssueIDs = myzephyr.getCycleIdByExecutions(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		
		/**
		 * 2. Let's Update the Status of Test Cases
		 * a. Fetch the HashMap for Test and Ticket Mapping.
		 * b. Fetch the HashMap for Issue Key (Ticket) and Issue Id mapping.
		 * c. From a & b above get the Result (Pass/Fail) for Test and put it in the status below. 
		 */
		//HashMap<String, String> myIssuesKeys = JIRATestCases.getIssueKeys(JIRAMetaData.getProjectName(projectId));
		
		Map<String, String> myIssuesKeys = getAllIssueKeysAndSummaryAssignedToACycle();
		
		for (String myIssueKey : myIssuesKeys.keySet()) 
		{
		Map<String, String> myIssueKeyIdMapping = myzephyr.getIssueKeyIdMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		String myIssueId = myIssueKeyIdMapping.get(myIssueKey);
		String myExecutionId = myIssueIDs.get(myIssueId);
		
		Map<String, String> myTestSteps = myzephyr.getStepResultsByExecution("/public/rest/api/1.0/stepresult/search?executionId="+myExecutionId+"&issueId="+myIssueId+"&isOrdered=true");
		Map<String, String[]> myTestStepsMetadata = myzephyr.getAllTeststeps("/public/rest/api/1.0/teststep/"+myIssueId+"?projectId="+projectId);
		
		for (String key : myTestStepsMetadata.keySet()) 
			{
			JSONObject executeTestsObj = new JSONObject();
			executeTestsObj.put("issueId", myIssueId);
			executeTestsObj.put("stepId", key);
			executeTestsObj.put("executionId",myExecutionId) ;
			executeTestsObj.put("comment", "Executed by MSG QA Automation Team.");
			JSONObject statusObj = new JSONObject();
			statusObj.put("id", "-1");
			executeTestsObj.put("status", statusObj);	
			StringEntity executeTestsJSON = null;
				try {
					executeTestsJSON = new StringEntity(executeTestsObj.toString());
					} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				myzephyr.putExecutionsForTestStep("/public/rest/api/1.0/stepresult/"+myTestSteps.get(key), executeTestsJSON);
			}
		}
	}	
	
/**
 * Get Cycle id	
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public static String getcycleId(String Cycle, int projectId, int versionId) 
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();		
	Map<String, String> myMappingList = new HashMap<String, String>();	
	try {
		myMappingList = myzephyr.getAllCycles("/public/rest/api/1.0/cycles/search?versionId="+versionId+"&projectId="+projectId);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
	return myMappingList.get(Cycle);
}

/**
 * Get All Test CAse Names for a Test Cycle
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public List<String> getAllTestCaseNamesForATestCycle() 
{
	List<String> testNames = new ArrayList<String>();
	ZephyrUtilities myzephyr = new ZephyrUtilities();	
	Map<String, String> myIssuesWithId = new HashMap<String, String>();
	try {
		myIssuesWithId = myzephyr.getAllIssuesAssignedToACycle("/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
	
		for(String key: myIssuesWithId.keySet())
		{
			testNames.add(key);
		}
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
	return testNames;
}

/**
 * Get All Test CAse Names for a Test Cycle with Test Case Ids
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public Map<String, String> getAllIssueSummaryAndKeysAssignedToACycle() 
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();	
	Map<String, String> myIssuesWithId = new HashMap<String, String>();
	try {
		myIssuesWithId = myzephyr.getAllIssueSummaryAndKeysAssignedToACycle("/public/rest/api/1.0/executions/search/cycle/"+MSGJIRAupdate.cycleId+"?versionId="+versionId+"&projectId="+projectId);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
	return myIssuesWithId;
}

/**
 * Get All Test CAse Names for a Test Cycle with Test Case Ids
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public Map<String, String> getAllIssueKeysAndSummaryAssignedToACycle() 
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();	
	Map<String, String> myIssuesWithId = new HashMap<String, String>();
	try {
		myIssuesWithId = myzephyr.getAllIssueKeysAndSummaryAssignedToACycle("/public/rest/api/1.0/executions/search/cycle/"+MSGJIRAupdate.cycleId+"?versionId="+versionId+"&projectId="+projectId);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
	return myIssuesWithId;
}

/**
 * Get All Test CAse Names for a Test Cycle with Test Case Ids
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public Map<String, String> getAllTestCaseNamesWithTestCaseIdForATestCycle() 
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();	
	Map<String, String> myIssuesWithId = new HashMap<String, String>();
	try {
		myIssuesWithId = myzephyr.getAllIssuesAssignedToACycle("/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
	return myIssuesWithId;
}

/**
 * Get All Test Steps for a Test Case
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public List<String> getAllTestStepNamesForATestCase(String cycleId, String testCase) 
{
	List<String> testStepNames = new ArrayList<String>();
	ZephyrUtilities myzephyr = new ZephyrUtilities();		
	try {
		
		// 1. Find in all issueSummary the matching Test Case with coresponding key.
		Map<String, String[]> myExecutionIds = myzephyr.getExecutionsForCycle("/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		
		for(String key: myExecutionIds.keySet())
		{
			String[] myIssueData = new String[4];
			myIssueData = myExecutionIds.get(key);
			
			// 2. Check if the Test case is matched with name supplied at input.
			if (myIssueData[2].equals(testCase))
			{
				Map<String, String[]> myTestStepsMetadata = myzephyr.getAllTeststeps("/public/rest/api/1.0/teststep/"+myIssueData[0]+"?projectId="+projectId);	
				for(String id: myTestStepsMetadata.keySet())
				{
					String[] issueStep = new String[2];
					issueStep = myTestStepsMetadata.get(id);
					testStepNames.add(issueStep[1]);
				}
			}	
		}
		
		} 
	catch (URISyntaxException e) {
		e.printStackTrace();
	}
	
	return testStepNames;
}

/**
 * Get All Test Cases of a test Cycle
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public List<String> getAllCycleNamesForAProject() 
{
	List<String> cycleNames = new ArrayList<String>();
	ZephyrUtilities myzephyr = new ZephyrUtilities();		
	Map<String, String> myMappingList = new HashMap<String, String>();	
	try {
		myMappingList = myzephyr.getAllCycles("/public/rest/api/1.0/cycles/search?versionId="+versionId+"&projectId="+projectId);
		if (myMappingList !=null)
		{
			for(String Key: myMappingList.keySet())
			{
				cycleNames.add(Key);
			}	
		}
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
		
	return cycleNames;
}

/**
 * Get All Cycles and Cycle Ids for a Project
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 */
public Map<String, String> getAllCycleNamesAndCycleIdForAProject() 
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();		
	Map<String, String> myMappingList = new HashMap<String, String>();	
	try {
		myMappingList = myzephyr.getAllCycles("/public/rest/api/1.0/cycles/search?versionId="+versionId+"&projectId="+projectId);
		if (myMappingList !=null)
		{
			return myMappingList;
		}
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	
		
	return myMappingList;
}


/**
 * Update Status for single Test Case in Jira.
 * @param TestCase
 * @param Status
 * @throws JSONException
 * @throws URISyntaxException
 * @throws java.text.ParseException
 * @throws ParseException
 * @throws IOException
 */
public void updateStatusForSingleTestCasesInJiraSuites(String TestCase, String Status) throws JSONException, URISyntaxException, java.text.ParseException, ParseException, IOException
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();	
	
	/**
	 * 1. Fetch Test Cases for Rockettes - SmokeTesting Cycle
	 */
	Map<String, String> myExecutionIds = myzephyr.getExecutionsByCycleId(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
	
	/**
	 * 2. Let's Update the Status of Test Cases
	 * a. Fetch the HashMap for Test and Ticket Mapping.
	 * b. Fetch the HashMap for Issue Key (Ticket) and Issue Id mapping.
	 * c. From a & b above get the Result (Pass/Fail) for Test and put it in the status below. 
	 */
	
	//HashMap<String, String> myTestCases = JIRATestCases.getTestCases(JIRAMetaData.getProjectName(projectId));
	
	Map<String, String> myTestCases = getAllIssueSummaryAndKeysAssignedToACycle();

	String myIssueKey = null;
	
	for(String key: myTestCases.keySet())
	{
		if(key.contains(TestCase))
		{
			myIssueKey = myTestCases.get(key);
			break;
		}
	}
	
	Map<String, String> myIssueKeyIdMapping = myzephyr.getIssueKeyIdMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
	
	JSONObject executeTestsObj = new JSONObject();
	
	executeTestsObj.put("cycleId", cycleId);
	executeTestsObj.put("projectId", projectId);
	executeTestsObj.put("versionId", versionId);
	executeTestsObj.put("comment", "Executed by MSG QA Automation Team.");
	
	if(Status == "PASS")
	{
		JSONObject statusObj = new JSONObject();
		statusObj.put("id", "1");
		executeTestsObj.put("status", statusObj);	
	}
	
	if(Status == "FAIL")
	{
		JSONObject statusObj = new JSONObject();
		statusObj.put("id", "2");
		executeTestsObj.put("status", statusObj);	
	}
	
	String myIssueId = myIssueKeyIdMapping.get(myIssueKey);
	executeTestsObj.put("issueId", myIssueId);
	StringEntity executeTestsJSON = null;
	try {
		executeTestsJSON = new StringEntity(executeTestsObj.toString());
		} catch (UnsupportedEncodingException e1) {
		e1.printStackTrace();
	}
	for (String key : myExecutionIds.keySet()) 
	{
		if (myExecutionIds.get(key).equals(myIssueId))
		{
			myzephyr.putExecutionsForCycle(11001, 11500, "/public/rest/api/1.0/execution/"+key, executeTestsJSON);
		}
	}
}

/**
 * This Section Covers the Methods Related to the JIRA Reporting
 * @param Cycle
 * @param projectId
 * @param versionId
 * @return
 * @throws URISyntaxException 
 */

public List<List<String>> getJiraReportingData() throws URISyntaxException 
{
	List<List<String>> myJiraExecutionSummary = new ArrayList<List<String>>();
	
	// 1. Get the Cycle Name back from cycle Id.
	ZephyrUtilities myzephyr = new ZephyrUtilities();		
	Map<String, String> myMappingList = new HashMap<String, String>();	
	try {
		myMappingList = myzephyr.getAllCyclesIdWithNameMapping("/public/rest/api/1.0/cycles/search?versionId="+versionId+"&projectId="+projectId);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}	

	myMappingList.get(cycleId);
	
	// 2. Get the corresponding executions.
	myJiraExecutionSummary = myzephyr.getJIRAExecutionsZephyrData("/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId, cycleName);
	
	return  myJiraExecutionSummary;
}

/**
 * Generate HTML Report
 * @throws URISyntaxException
 */
public void generateHTMLJIRAReport() throws URISyntaxException
{
	List<List<String>> myJiraExecutionSummary = getJiraReportingData();
	StringBuilder buf = new StringBuilder();
	buf.append("<html>" +
	           "<body>" +
	           "<table>" +
	           "<tr>" +
	           "<th>Project</th>" +
	           "<th>Test Cycle</th>" +
	           "<th>Test Case Key</th>" +
	           "<th>Test Case Description</th>" +
	           "<th>Test Status</th>" +
	           "<th>Test Executed By</th>" +
	           "<th>Test Executed On</th>" +
				"</tr>");
	for (int i = 0; i < myJiraExecutionSummary.size(); i++) 
	{
		List<String> myJiraReportingRecord = new ArrayList<String>();
		myJiraReportingRecord = myJiraExecutionSummary.get(i);
		buf.append("<tr>");
		for (int j = 0; j < myJiraReportingRecord.size(); j++) 
			{
			    buf.append("<td>");
			    
			    if(!myJiraReportingRecord.get(j).isEmpty())
			    {
			    	buf.append(myJiraReportingRecord.get(j));
			    }
			    else
			    {
			    	buf.append("NA");
			    }
			    buf.append("</td>");
			}
		buf.append("</tr>");
	}
	buf.append("</table>" +
	           "</body>" +
	           "</html>");
	String html = buf.toString();
	System.out.println(html);
}

/**
 * ZEPHYR META DATA CREATION:
 * Below section method will handle following:
 * 1. Creation of Test Cycle: Check if there is a cycle with user input name exist already then skip it Else create it. 
 * 2. Creation of Test Case (Issue Type Test)
 * 3. Creation of Test Step
 * @throws IOException 
 * @throws java.text.ParseException 
 * @throws URISyntaxException 
 * @throws ParseException 
 * @throws InterruptedException 
 *  
 */

public String createTestCycleInJira(String testCycleName) throws ParseException, URISyntaxException, java.text.ParseException, IOException, InterruptedException
{
	String cycleId = null;
	boolean isCycleExistingAlready = false;
	
	// 1. GET: Check for existing test Cycles and see if there is a matching cycle in that case skip creation.
	if(getAllCycleNamesForAProject().contains(testCycleName))
	{
		isCycleExistingAlready = true;
		return getAllCycleNamesAndCycleIdForAProject().get(testCycleName);
	}
	
	// 2. POST: Create the Test Cycle if it;s not existing already.
	if (!isCycleExistingAlready)
	{
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		JSONObject executeTestsObj = new JSONObject();
		executeTestsObj.put("name", testCycleName);
		executeTestsObj.put("projectId", projectId);
		executeTestsObj.put("versionId", versionId);
		executeTestsObj.put("description", "Powered by - MSG QA Automation Team");
		StringEntity executeTestsJSON = null;
		try {
			executeTestsJSON = new StringEntity(executeTestsObj.toString());
			} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		cycleId = myzephyr.createTestCycle("/public/rest/api/1.0/cycle", executeTestsJSON);
	}
	return cycleId;
}

/**
 * Create Test Cycle with Start and End Date
 * @param testCycleName
 * @param startDate
 * @param endDate
 * @return
 * @throws ParseException
 * @throws URISyntaxException
 * @throws java.text.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public String createTestCycleInJira(String testCycleName, String startDate, String endDate) throws ParseException, URISyntaxException, java.text.ParseException, IOException, InterruptedException
{
	String cycleId = null;
	boolean isCycleExistingAlready = false;
	
	// 1. GET: Check for existing test Cycles and see if there is a matching cycle in that case skip creation.
	if(getAllCycleNamesForAProject().contains(testCycleName))
	{
		isCycleExistingAlready = true;
		return getAllCycleNamesAndCycleIdForAProject().get(testCycleName);
	}
	
	// 2. POST: Create the Test Cycle if it;s not existing already.
	if (!isCycleExistingAlready)
	{
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		JSONObject executeTestsObj = new JSONObject();
		executeTestsObj.put("name", testCycleName);
		executeTestsObj.put("projectId", projectId);
		executeTestsObj.put("versionId", versionId);
		
		// Conversion of Start and End Date to assign to Test Cycle
		try {
			// 2017-04-11T10:36:40.532-04:00
			
		    Long startDateEpoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(startDate).getTime();
		    Long endDateEpoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(endDate).getTime();
		    executeTestsObj.put("startDate", startDateEpoch);
		    executeTestsObj.put("endDate", endDateEpoch);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
			
		
		executeTestsObj.put("description", "Powered by - MSG QA Automation Team");
		StringEntity executeTestsJSON = null;
		try {
			executeTestsJSON = new StringEntity(executeTestsObj.toString());
			} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		cycleId = myzephyr.createTestCycle("/public/rest/api/1.0/cycle", executeTestsJSON);
	}
	return cycleId;
}

/**
 * Create Test Case in JIRA
 * @param testCase
 * @throws ParseException
 * @throws URISyntaxException
 * @throws java.text.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public String createTestCaseInJira(String testCase) throws ParseException, URISyntaxException, java.text.ParseException, IOException, InterruptedException
{
	String testCaseIssueId = null;
	boolean isTestCaseExistingAlready = false;
	String testCaseIssueIdLong = null;
	
	// 1. GET: Check for existing test Cases and see if there is a matching Case in that case skip creation.
	if(getAllTestCaseNamesForATestCycle().contains(testCase))
	{
		isTestCaseExistingAlready = true;
		return getAllTestCaseNamesWithTestCaseIdForATestCycle().get(testCase);
	}
	
	// 2. POST: Create the Test Case if it's not existing already.
	if (!isTestCaseExistingAlready)
	{
		// 3. Create a Bug Card in JIRA for failed scenarios by utilizing the JIRA REST Capabilities & Add the Test Case to the Cycle
		ZephyrUtilities myzephyr = new ZephyrUtilities();	
		JSONObject executeTestsObj = new JSONObject();
		JSONArray  issuesArray = new JSONArray();
		testCaseIssueId = jiraUtilities.createTestInJira(projectId, componentId, "msgtesting", testCase);
		issuesArray.put(testCaseIssueId);
		executeTestsObj.put("issues", issuesArray);
		executeTestsObj.put("projectId", projectId);
		executeTestsObj.put("versionId", versionId);
		executeTestsObj.put("method", "1");
		StringEntity executeTestsJSON = null;
		try {
			executeTestsJSON = new StringEntity(executeTestsObj.toString());
			} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		myzephyr.assignTestCaseToACycle(testCase, "/public/rest/api/1.0/executions/add/cycle/"+cycleId, executeTestsJSON);
		Map<String, String> myIssueKeyIdMapping = myzephyr.getIssueKeyIdMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
		testCaseIssueIdLong = myIssueKeyIdMapping.get(testCaseIssueId);
	}
	return testCaseIssueIdLong;
}

/**
 * Create Test Step in JIRA
 * @param testCase
 * @throws ParseException
 * @throws URISyntaxException
 * @throws java.text.ParseException
 * @throws IOException
 * @throws InterruptedException 
 */
public void createTestStepInJira(String testCaseIssueId, String testCase, String testStepName, String testStepData, String testStepResult) throws ParseException, URISyntaxException, java.text.ParseException, IOException, InterruptedException
{
	boolean isTestStepExistingAlready = false;
	
	// 1. GET: Check for existing test Step and see if there is a matching Step in that case skip creation.
	if(getAllTestStepNamesForATestCase(cycleId, testCase).contains(testStepName))
	{
		isTestStepExistingAlready = true;
	}
	
	// 2. POST: Create the Test Step if it's not existing already.
	if (!isTestStepExistingAlready)
	{
				ZephyrUtilities myzephyr = new ZephyrUtilities();	
				JSONObject executeTestsObj = new JSONObject();
				executeTestsObj.put("step", testStepName);
				executeTestsObj.put("data", testStepData);
				executeTestsObj.put("result", testStepResult);
				StringEntity executeTestsJSON = null;
				try {
					executeTestsJSON = new StringEntity(executeTestsObj.toString());
					} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
					
				myzephyr.assignTestStepToTestCase("/public/rest/api/1.0/teststep/"+testCaseIssueId+"?projectId="+projectId, executeTestsJSON);
				
	}
}

public void createAutomaticTestCasesUsingSprintDataInAProject(String BoardName) throws URISyntaxException, ParseException, java.text.ParseException, IOException, InterruptedException
{
	// 1. Find the Board out of all available Boards of the project.
	String myBoardId = jiraUtilities.getAllBoardNameAndIdsForAProject().get(BoardName);
	String myTestCycleName, myTestCycleStartDate, myTestCycleEndDate;
	String myTestCaseName, myTestCaseLinkedUserStory, myTestCaseLinkedUserStoryOwner;

	if(myBoardId == null)
	{
		System.out.println("Board is not found, exiting the creation of Test Meta data!!");
	}
	else
	{
		System.out.println("Board '"+BoardName+"' is found, searching the Active Sprint (if any)..");
	
		// 2. Find if there is an Active sprint in the Board.
		String[] mySprintDetails = jiraUtilities.getActiveSprintNameAndIdsForABoard(myBoardId).get(myBoardId);

		if(mySprintDetails[0] !=null)
		{
			System.out.println("Active Sprint '"+mySprintDetails[1]+"' with validity as ("+mySprintDetails[2]+" : "+mySprintDetails[3]+") is found, searching the User stories assigned (if any)..");
			
			myTestCycleName = mySprintDetails[1];
			myTestCycleStartDate = mySprintDetails[2];
			myTestCycleEndDate = mySprintDetails[3];
	
			// 2.1 Create Test Cycle with the above parameters.
			cycleId = createTestCycleInJira(myTestCycleName,myTestCycleStartDate,myTestCycleEndDate);
			
			// 3. Find if there are any User Stories assigned in the Sprint
			HashMap<String, String[]> myUserStories = jiraUtilities.getAllIssuesInActiveSprintOfABoard(myBoardId, mySprintDetails[0], true);
			System.out.println("Sprint ("+mySprintDetails[1]+") contains following User Stories...");
			int stepCount = 1;
			for(String IssueSummary: myUserStories.keySet())
			{
				String[] myIssueDetails = myUserStories.get(IssueSummary);
				System.out.println("Action_Log"+stepCount+":");
				System.out.println("User Story ("+myIssueDetails[1]+"): "+IssueSummary+" [Status: "+myIssueDetails[2]+"]");
				
				// 4. If User stories are found then create Test Cases for them.
				String myTestCaseIssueId = createTestCaseInJira(IssueSummary);
				
				// 5. Link User story with Test case.
				ZephyrUtilities myzephyr = new ZephyrUtilities();
				Map<String, String> myIssueIdKeyMapping = myzephyr.getIssueIdKeyMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
				jiraUtilities.linkIssueWithUserStoryInJira(myIssueIdKeyMapping.get(myTestCaseIssueId), myIssueDetails[1]);
				
				stepCount++;
			}
		}
		else
		{
			System.out.println("No Active Sprints are found, exiting the creation of Test Meta data!!");		
		}
	}
}

public void createAnAttachmentInJira(String issueKey, String attachmentPath) throws JSONException, URISyntaxException, java.text.ParseException, IOException
{
	ZephyrUtilities myzephyr = new ZephyrUtilities();
	
	//myzephyr.addAttachment(projectId, versionId, cycleId, issueId, entityId, resource);
	
	Map<String, String> myIssueKeyIdMapping = myzephyr.getIssueKeyIdMapping(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
	
	String myIssueId = myIssueKeyIdMapping.get(issueKey);
	
	Map<String, String> myIssueIDs = myzephyr.getCycleIdByExecutions(projectId, versionId, "/public/rest/api/1.0/executions/search/cycle/"+cycleId+"?versionId="+versionId+"&projectId="+projectId);
	
	String myExecutionId = myIssueIDs.get(myIssueId);
	
	myzephyr.addAttachment(projectId, versionId, cycleId, myIssueId, myExecutionId, attachmentPath);
}

public void setCycleId(String cycleId)
{
	MSGJIRAupdate.cycleId = cycleId;
}

public String getCycleId()
{
	return MSGJIRAupdate.cycleId;
}


}
