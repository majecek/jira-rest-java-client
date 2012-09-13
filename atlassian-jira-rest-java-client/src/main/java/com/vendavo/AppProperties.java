/**
 * 
 */
package com.vendavo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author cdome
 *
 */
public class AppProperties {
	private static final AppProperties instance = new AppProperties();
	
	private final Properties appProps = new Properties();
	private final Properties pwdProps = new Properties();
	
		
	public static AppProperties getInstance() {
		return instance;
	}
	
	private AppProperties() {
		try {
    		appProps.load(getClass().getClassLoader().getResourceAsStream("app.properties"));
    		pwdProps.load(getClass().getClassLoader().getResourceAsStream("pwd.properties"));
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
	}
	
	public String getJiraURL() {
		return appProps.getProperty("jira.url");
	}
	
	public String getJiraUser() {
		return pwdProps.getProperty("jira.user");
	}
	
	public String getJiraPassword() {
		return pwdProps.getProperty("jira.pwd");
	}
}
