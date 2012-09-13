package com.vendavo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.AuthenticationException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.ProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicComponent;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.Version;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

public class VendavoClazz {

	private String fixVersion;
	private boolean isSubtask;
	private String JQL;
	private String url;
	private String username;
	private String password;
	private String assignee;
	
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFixVersion() {
		return fixVersion;
	}
	public void setFixVersion(String fixVersion) {
		this.fixVersion = fixVersion;
	}
	public boolean isSubtask() {
		return isSubtask;
	}
	public void setSubtask(boolean isSubtask) {
		this.isSubtask = isSubtask;
	}
	public String getJQL() {
		return JQL;
	}
	public void setJQL(String jQL) {
		JQL = jQL;
	}


	/**
	 * create new issue
	 * @return collection with list of newly created issues
	 * @throws URISyntaxException
	 * @throws AuthenticationException
	 */
	public Collection<String> createIssue() throws URISyntaxException, AuthenticationException {
		Collection<String> result = new ArrayList<String>();
		
		JerseyJiraRestClientFactory f = new JerseyJiraRestClientFactory();
		JiraRestClient jc = f.createWithBasicHttpAuthentication(new URI(getUrl()), getUsername(), getPassword());
		ProgressMonitor pm = new NullProgressMonitor();
		SearchResult r = jc.getSearchClient().searchJql(getJQL(), null);
		
		Iterator<BasicIssue> it = r.getIssues().iterator();
		while (it.hasNext()) {
			Issue issue = jc.getIssueClient().getIssue(((BasicIssue) it.next()).getKey(), null);
			Iterable<Version> versions = jc.getIssueClient().getVersions(issue.getProject().getKey(), pm);
			if (checkVersion(getFixVersion(), versions)) {
				try {
					JSONObject json = createJSONFromIssue(issue, isSubtask(), getFixVersion());
					String issueID = jc.getIssueClient().createIssue(pm, json);
					jc.getIssueClient().updateAssigee(pm, getAssignee(), issueID);
					result.add(issueID);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("FixVersion [" + getFixVersion() + "] doesn't exists for project: " + issue.getProject().getKey());
				throw new IllegalArgumentException("FixVersion [" + getFixVersion() + "] doesn't exists for project: " + issue.getProject().getKey());
			}
		}
		return result;
	}
	
	
	/**
	 * check whether version exists in given project
	 * @param fixVersion
	 * @param versions
	 * @return
	 */
	private boolean checkVersion(String fixVersion, Iterable<Version> versions) {
		for (Version version : versions) {
			if(version.getName().equals(fixVersion)) return true;
		}
		return false;
	}
	
	
	/**
	 * Create JSON object for given issue
	 * @param issue
	 * @param subtask
	 * @param version
	 * @return
	 * @throws JSONException
	 * @throws org.codehaus.jettison.json.JSONException 
	 */
	private JSONObject createJSONFromIssue(Issue issue, Boolean subtask, String version) throws org.codehaus.jettison.json.JSONException {
		String name = "name";
		String key = "key";
		String property = "project";
		String description = "description";
		String summary = "summary";
		String components = "components";
		String versions = "versions";
		String parent = "parent";
		String issuetype = "issuetype";
		String fields = "fields";
		String subtaskString = "Sub-task";
		String bug = "Bug";
		
		JSONObject json = new JSONObject();
		JSONObject fieldsJS = new JSONObject();
		JSONObject projectJS = new JSONObject();
		JSONObject issuetypeJS = new JSONObject();
		JSONObject parentJS = new JSONObject();
		JSONObject componentsJS = new JSONObject();
		JSONArray componentsJSArray = new JSONArray();
		JSONArray versionJSArray = new JSONArray();
		JSONObject versionsJS = new JSONObject();
		
		
		componentsJS.put(name, ((BasicComponent) issue.getComponents().iterator().next()).getName());
		componentsJSArray.put(componentsJS);
		projectJS.put(key, issue.getProject().getKey());
		versionsJS.put(name, version);
		versionJSArray.put(versionsJS);
		
		fieldsJS.put(property, projectJS);
		fieldsJS.put(description, "See the parent task for description.");
		String summaryString = issue.getSummary();
		if (summaryString.length() > 240) {
			summaryString = summaryString.substring(0, 240);
		}
		fieldsJS.put(summary, "port to "+ version +": " + summaryString);
		
		if (subtask) {
			issuetypeJS.put(name, subtaskString);
			parentJS.put(key, issue.getKey());
			fieldsJS.put(parent, parentJS);
		} else {
			issuetypeJS.put(name, bug);
		}
		
		fieldsJS.put(issuetype, issuetypeJS);
		fieldsJS.put(components, componentsJSArray);
		fieldsJS.put(versions, versionJSArray);
		
		json.put(fields, fieldsJS);
		return json;
	}
	


	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws JSONException 
	 * @throws ClientHandlerException 
	 * @throws AuthenticationException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws URISyntaxException, AuthenticationException, IOException {
		long a = System.currentTimeMillis();
		VendavoClazz  vendavoJira = new VendavoClazz();
		System.out.print("Enter your query: ");
		String query = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		
		vendavoJira.setPassword(AppProperties.getInstance().getJiraPassword());
		vendavoJira.setUsername(AppProperties.getInstance().getJiraUser());
		vendavoJira.setUrl(AppProperties.getInstance().getJiraURL());
		vendavoJira.setFixVersion("8.1");
//		"project = ven AND fixVersion = '7.6 MP1'"
		vendavoJira.setJQL(query);
		vendavoJira.setSubtask(true);
		vendavoJira.setAssignee(AppProperties.getInstance().getJiraUser());
		Collection<String> result = vendavoJira.createIssue();
		long b = System.currentTimeMillis() - a;

		System.out.println("Created Issues JQL: key in ("+result+")");
		System.out.println("milsec:" + b + "  min:" +b/1000/60);
		
		
	}

}
