jira-rest-java-client
=====================

https://plugins.atlassian.com/plugins/com.atlassian.jira.jira-rest-java-client


This project is fork of jira project. 
I have added few missing methods like: 
* createIssue
* updateAssignee

We use it internaly at work so this helps us. 

To make it run do these steps: 

1) cd src/main/resources/
2) you need to have 2 files here: 
app.properties 
** jira.url=http://10.40.2.27/
pwd.properties
** jira.user=username
** jira.pwd=password