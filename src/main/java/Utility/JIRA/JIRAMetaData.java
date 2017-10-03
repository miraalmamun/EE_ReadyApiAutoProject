package Utility.JIRA;

/**
 * This Utility class is primarily for the methods that may perform CRUD operation on JIRA.
 * @author Rachit Kumar Rastogi
 * @version 1.0 
 */

public class JIRAMetaData {
	
	/**
	 * Maintain the Project specific methods here -  Name and Id Mapping
	 * @param Project
	 * @return
	 */
	
	public static int getProjectId(String Project)
	{
		int ProjectId = 0;
		
		if(Project.equals("Rockettes"))
		{
			ProjectId = 11001;
		}
		
		if(Project.equals("MyMSGSuites"))
		{
			ProjectId = 11200;
		}
		
		if(Project.equals("MSGAutomation"))
		{
			ProjectId = 13500;
		}
		
		if(Project.equals("EventEngine"))
		{
			ProjectId = 13000;
		}
		
		if(Project.equals("MSGCom"))
		{
			ProjectId = 12900;
		}
		
		if(Project.equals("SalesCenter"))
		{
			ProjectId = 14100;
		}
		return ProjectId;
	}
	
	/**
	 * Maintain the Project specific methods here - Id and Name Mapping
	 * @param Project
	 * @return
	 */
	
	public static String getProjectName(int projectId)
	{
		String Project = null;
		
		if ( projectId == 11001 )
		{
			Project = "Rockettes";	
		}
		
		if ( projectId == 11200 )
		{
			Project = "MyMSGSuites";	
		}
		
		if ( projectId == 13500 )
		{
			Project = "MSGAutomation";	
		}
		
		if ( projectId == 13000 )
		{
			Project = "EventEngine";	
		}
		
		if ( projectId == 12900 )
		{
			Project = "MSGCom";	
		}
		
		if ( projectId == 14100 )
		{
			Project = "SalesCenter";	
		}
		
		return Project;
	}
	
	/**
	 * Maintain the Component specific methods here.
	 * @param Project
	 * @return
	 */
	
	public static String getComponentId(String Component)
	{
		String ComponentId = null;
		
		if(Component.equals("Rockettes"))
		{
			ComponentId = "11800";
		}
		
		if(Component.equals("MyMSGSuites"))
		{
			ComponentId = "";
		}
		
		if(Component.equals("MSGAutomation"))
		{
			ComponentId = "";
		}
		
		if(Component.equals("EventEngine"))
		{
			ComponentId = "";
		}
		
		if(Component.equals("MSGCom"))
		{
			ComponentId = "";
		}
		
		if(Component.equals("SalesCenter"))
		{
			ComponentId = "";
		}
		
		return ComponentId;
	}
	
}
