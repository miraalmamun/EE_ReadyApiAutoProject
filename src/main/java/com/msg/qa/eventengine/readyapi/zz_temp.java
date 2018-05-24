package com.msg.qa.eventengine.readyapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import Utility.JIRA.MSGJIRAupdate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class zz_temp {

	
	static String projectID = "EventEngine";
	
	//static String versionID = "Gretzky 2.11.0";
	//static String versionID = "14702";
	static String versionID = "-1";

	
	//static String cycleID = System.getProperty("testCycle"); // Get From Jenkins
	static String cycleID = "Smoke_TestSuite_Template";
	//static String cycleID = "REgression_TestSuite_Template";
	//static String cycleID = "Ad hoc";



	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException 
	{
		System.out.println("==================XX Jira Reporting Class XX================");

		try 
		{
			String TestcaseName = "Test case From Java 6";
			MSGJIRAupdate mJU = new MSGJIRAupdate(projectID, Integer.parseInt(versionID), cycleID);
			
			/* //==Create Test Case
			String testCaseIssueId=mJU.createTestCaseInJira(TestcaseName);
			
			//==Create Steps Inside Test case 
			mJU.createTestStepInJira(testCaseIssueId, TestcaseName, "Sample Step 1", "Sample Data", "Expected Result	: ");
			
			//Update Status ( Pass Or FAIL) for Test case & Test Steps  
			mJU.updateStatusForSingleTestStepsInJira(TestcaseName, "Sample Step 1", "FAIL");
			mJU.updateStatusForSingleTestCasesInJira(TestcaseName, "FAIL");*/

			
			File file = new File("//Users//rasulm//eclipse-workspace//MSGEventEngine//EE_SoapReadyAPIProjects//EE_ReadyAPIProjects_V2.0_Composit//reports//readyapireports//Event-Engine-API-Calls-V25x//testSuiteResults.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			System.out.println("TestSuiteResults XML's Root Node Name :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) 
			{
				NodeList nList = doc.getElementsByTagName("testCase");
				for (int temp = 0; temp < nList.getLength(); temp++)
				{
					Node nTestCaseNode = nList.item(temp);
					System.out.println("\nStart Parsing\t\t:\t" + nTestCaseNode.getNodeName() + " (" + (temp+1) + ")");
					System.out.println("=======================================================================");

					if (nTestCaseNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element eElement = (Element) nTestCaseNode;
						
						//==> Getting Test case name From XML File and Creating a Test case in Jira Zephyer 
						TestcaseName = eElement.getElementsByTagName("testCaseName").item(0).getTextContent();
						System.out.println("Test Case Name\t\t:\t" + TestcaseName);
						String testCaseIssueId=mJU.createTestCaseInJira(TestcaseName); //***Jira Update Code 
						
						//String stepDetails = eElement.getElementsByTagName("testStepResults").item(0).getTextContent();
						//System.out.println("Test Steps Details : " + stepDetails);
						Node exec = eElement.getElementsByTagName("testStepResults").item(0);
						//System.out.println(exec.getChildNodes().getLength());
						//System.out.println(exec.getTextContent());
						NodeList steps = exec.getChildNodes();
						int stepcount=1;
						for (int x = 0; x < steps.getLength(); x++)
						{
							if (steps.item(x).getNodeType() == Node.ELEMENT_NODE)
							{
								Element eElementx = (Element) steps.item(x);

								//==> Getting Test Steps From XML File and Creating Steps inside the Above Created Test case in Jira Zephyer
								String stepName = eElementx.getElementsByTagName("name").item(0).getTextContent();
								String stepMessage = eElementx.getElementsByTagName("message").item(0).getTextContent();
								String stepStatus = eElementx.getElementsByTagName("status").item(0).getTextContent();
								System.out.println("Test Case Step "+stepcount+" Name\t:\t" + stepName);
								System.out.println("Test Case Step "+stepcount+" Result\t:\t" + stepMessage );
								System.out.println("Test Case Step "+stepcount+" Status\t:\t" + stepStatus );
								mJU.createTestStepInJira(testCaseIssueId, TestcaseName, stepName, stepMessage, stepStatus);	//***Jira Update Code 
								
								//==> Update Test case Result In Jira 
								if(stepStatus.equalsIgnoreCase("OK"))
								{
									mJU.updateStatusForSingleTestStepsInJira(TestcaseName, stepName, "PASS");;
								}
								else
								{
									mJU.updateStatusForSingleTestStepsInJira(TestcaseName, stepName, "FAIL");;
								}
								
								
								stepcount++;
							}
						}
						
						//==> Update Test case Result In Jira 
						String TestcaseStatus = eElement.getElementsByTagName("status").item(0).getTextContent();
						System.out.println("Test Case Status\t:\t" + TestcaseStatus );
						if(TestcaseStatus.equalsIgnoreCase("FINISHED"))
						{
							mJU.updateStatusForSingleTestCasesInJira(TestcaseName, "PASS");
						}
						else
						{
							mJU.updateStatusForSingleTestCasesInJira(TestcaseName, "FAIL");
						}
						
						//System.out.println("Test Case TimeTaken\t:\t" + eElement.getElementsByTagName("timeTaken").item(0).getTextContent());
					}
				}
				//printNote(doc.getChildNodes());
			}
		    } catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		//System.out.println("********* Reasul Test Posting Ready Api Reports********");
		
		//String testCompleteResults= "";
		//testCompleteResults = fileToString("//Users//rasulm//git//EE_ReadyApiAutoProject//reports//readyapireports//Event-Engine-API-Calls-V25x//testSuiteResults.xml");
		//System.out.println(testCompleteResults);
		
		//String nodevalue[] = fGetXMLElementValues(testCompleteResults,"testCaseName");
		
		 //HashMap<String, HashMap<String, String>> testCompleteResultsHashMap = new HashMap<String, HashMap<String, String>>();
		 //testCompleteResultsHashMap = parseTestCaseResults(testCompleteResults);

	}
	

	protected static Node getNode(String tagName, NodeList nodes) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	 
	    return null;
	}
	
	protected static String getNodeValue( Node node ) {
	    NodeList childNodes = node.getChildNodes();
	    for (int x = 0; x < childNodes.getLength(); x++ ) {
	        Node data = childNodes.item(x);
	        if ( data.getNodeType() == Node.TEXT_NODE )
	            return data.getNodeValue();
	    }
	    return "";
	}
	
	protected static String getNodeValue(String tagName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.TEXT_NODE )
	                    return data.getNodeValue();
	            }
	        }
	    }
	    return "";
	}
	
	protected String getNodeAttr(String attrName, Node node ) {
	    NamedNodeMap attrs = node.getAttributes();
	    for (int y = 0; y < attrs.getLength(); y++ ) {
	        Node attr = attrs.item(y);
	        if (attr.getNodeName().equalsIgnoreCase(attrName)) {
	            return attr.getNodeValue();
	        }
	    }
	    return "";
	}
	
	protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
	                    if ( data.getNodeName().equalsIgnoreCase(attrName) )
	                        return data.getNodeValue();
	                }
	            }
	        }
	    }
	 
	    return "";
	}
	
	private static void printNote(NodeList nodeList) 
  {

	    for (int count = 0; count < nodeList.getLength(); count++) 
	    {

		Node tempNode = nodeList.item(count);

		// make sure it's element node.
		if (tempNode.getNodeType() == Node.ELEMENT_NODE) 
		{

			// get node name and value
			System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
			System.out.println("Node Value =" + tempNode.getTextContent());
			
			
			/*if (tempNode.hasAttributes()) {

				// get attributes names and values
				NamedNodeMap nodeMap = tempNode.getAttributes();

				for (int i = 0; i < nodeMap.getLength(); i++) {

					Node node = nodeMap.item(i);
					System.out.println("attr name : " + node.getNodeName());
					System.out.println("attr value : " + node.getNodeValue());

				}

			}*/

			if (tempNode.hasChildNodes()) {

				// loop again if has child nodes
				printNote(tempNode.getChildNodes());

			}

			System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

		}

	    }

	  }
	
	public static String fileToString(String filename) throws IOException 
    {
    	InputStream is = new FileInputStream(filename);
    	BufferedReader buf = new BufferedReader(new InputStreamReader(is));
    	String line = buf.readLine();
    	StringBuilder sb = new StringBuilder();
    	while(line != null){
    		sb.append(line).append("\n");
    		line = buf.readLine();
    		}
    	String fileAsString = sb.toString();
    	return fileAsString;
    }

    public static HashMap<String, HashMap<String, String>> testCaseResultsMap = new HashMap<String, HashMap<String, String>>();

    public static HashMap<String, HashMap<String, String>> parseTestCaseResults(String testCaseResults) {
    	String testCaseResultsSplitString = testCaseResults;
    	int testCaseResultsSplitStringLength = testCaseResultsSplitString.length();

    	int TCNameLB = testCaseResults.indexOf("con:testCaseRunLog testCase=", 0);
    	int TCNameRB = testCaseResults.indexOf("timeTaken=", TCNameLB);
    	String tCName = testCaseResults.substring(TCNameLB+29, TCNameRB-2);

    	while (testCaseResultsSplitString.contains("<con:testCaseRunLogTestStep name=")) {

    		int TSNameLB = testCaseResultsSplitString.indexOf("<con:testCaseRunLogTestStep name=", 0);
        	int TSNameRB = testCaseResultsSplitString.indexOf("timeTaken", TSNameLB);
        	String tSName = testCaseResultsSplitString.substring(TSNameLB+34, TSNameRB-2);

        	int TSStatusLB = testCaseResultsSplitString.indexOf("status=", TSNameLB);
        	int TSStatusRB = testCaseResultsSplitString.indexOf("timestamp=", TSNameLB);
        	String tSStatus = testCaseResultsSplitString.substring(TSStatusLB+8, TSStatusRB-2);

        	int TSTimestampLB = testCaseResultsSplitString.indexOf("timestamp=", TSNameLB);
        	int TSTimestampRB = testCaseResultsSplitString.indexOf("endpoint=", TSNameLB);
        	String tSTimestamp = testCaseResultsSplitString.substring(TSTimestampLB+11, TSTimestampRB-2);

        	int TSEndpointLB = testCaseResultsSplitString.indexOf("endpoint=", TSNameLB);
        	int TSEndpointRB = testCaseResultsSplitString.indexOf("httpStatus=", TSNameLB);
        	String tSEndpoint = testCaseResultsSplitString.substring(TSEndpointLB+10, TSEndpointRB-2);

        	int TSHttpStatusLB = testCaseResultsSplitString.indexOf("httpStatus=", TSNameLB);
        	int TSHttpStatusRB = testCaseResultsSplitString.indexOf("contentLength=", TSNameLB);
        	String tSHttpStatus = testCaseResultsSplitString.substring(TSHttpStatusLB+12, TSHttpStatusRB-2);

        	HashMap<String, String> testStepResults = new HashMap<String, String>();

        	if (testCaseResultsSplitString.contains("FAILED")) {
            	int TSHttpFailureMessageLB = testCaseResultsSplitString.indexOf("<con:message>", TSNameLB);
            	int TSHttpFailureMessageRB = testCaseResultsSplitString.indexOf("</con:message>", TSNameLB);
            	String tSHttpFailureMessage = testCaseResultsSplitString.substring(TSHttpStatusLB+13, TSHttpStatusRB-1);
            	testStepResults.put("failure", tSHttpFailureMessage);
        	}
        	
        	testStepResults.put("name", tSName);
        	testStepResults.put("status", tSStatus);
        	testStepResults.put("timestamp", tSTimestamp);
        	testStepResults.put("endpoint", tSEndpoint);
        	testStepResults.put("httpstatus", tSHttpStatus);
        	
        	testCaseResultsMap.put(tCName + " - " + tSName, testStepResults);
        	
        	testCaseResultsSplitString = testCaseResultsSplitString.substring(TSNameRB);
    	}

    	return testCaseResultsMap;
    }

    public static String[] fGetXMLElementValues(String sXMLstring, String sElement)
    {
    		// split the string into an array based on the element name
    		String[] aTemp = sXMLstring.split(sElement);
    		
    		// create a new 10-element return array
    		String[] aRetVal = new String[10];
    		// set each value in the return array to NOTFOUND
    		//VB TO JAVA CONVERTER NOTE: The ending condition of VB 'For' loops is tested only on entry to the loop. VB to Java Converter has created a temporary variable in order to use the initial value of ubound(aRetVal) for every iteration:
    		int tempVar = aRetVal.length - 1;
    		for (int x = 0; x <= tempVar; x++)
    		{
    			aRetVal[x] = "NOTFOUNDD";
    		}
    		//  The relevant XML values are in the odd-number indicies. Trim those values of unneeded characters and save them into the array to be returned.
    		int iRetvalIndex = 0;
    //VB TO JAVA CONVERTER NOTE: The ending condition of VB 'For' loops is tested only on entry to the loop. VB to Java Converter has created a temporary variable in order to use the initial value of ubound(aTemp) for every iteration:
    		int tempVar2 = aTemp.length;
    		for (int i = 0; i <= tempVar2; i++)
    		{
    			if ((i % 2) != 0)
    			{
    				aTemp[i] = aTemp[i].replace("<", "");
    				aTemp[i] = aTemp[i].replace("/>", "");
    				aTemp[i] = aTemp[i].replace("/ >", "");
    				aTemp[i] = aTemp[i].replace("/  >", "");

    					if (! (aTemp[i].indexOf("DOCTYPE HTML PUBLIC") + 1>0))
    					{
    						aRetVal[iRetvalIndex] = aTemp[i];
    					}

    					iRetvalIndex = iRetvalIndex + 1;
    			}
    		}
    		// return the array
    		return aRetVal;
    }

  
	
}
