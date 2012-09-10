/*
 * Copyright (C) 2010 Atlassian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.jira.rest.client;

import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Transition;
import com.atlassian.jira.rest.client.domain.Version;
import com.atlassian.jira.rest.client.domain.Votes;
import com.atlassian.jira.rest.client.domain.Watchers;
import com.atlassian.jira.rest.client.domain.input.AttachmentInput;
import com.atlassian.jira.rest.client.domain.input.LinkIssuesInput;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.google.common.annotations.Beta;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import org.codehaus.jettison.json.JSONObject;


/**
 * The client handling issue resources.
 *
 * @since v0.1
 */
public interface IssueRestClient {
	/**
	 * Retrieves issue with selected issue key.
	 *
	 * @param issueKey issue key (like TST-1, or JRA-9)
	 * @param progressMonitor progress monitor  
	 * @return issue with given <code>issueKey</code>
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	Issue getIssue(String issueKey, ProgressMonitor progressMonitor);

	/**
	 * Retrieves issue with selected issue key, with specified additional expandos.
	 *
	 * @param issueKey issue key (like TST-1, or JRA-9)
	 * @param expand additional expands. Currently CHANGELOG is the only supported expand that is not expanded by default.
	 * @param progressMonitor progress monitor
	 * @return issue with given <code>issueKey</code>
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 * @since 0.6
	 */
	Issue getIssue(String issueKey, Iterable<Expandos> expand, ProgressMonitor progressMonitor);

	/**
	 * Retrieves complete information (if the caller has permission) about watchers for selected issue.
	 *
	 * @param watchersUri URI of watchers resource for selected issue. Usually obtained by calling <code>Issue.getWatchers().getSelf()</code>
	 * @param progressMonitor progress monitor  
	 * @return detailed information about watchers watching selected issue.
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 * @see com.atlassian.jira.rest.client.domain.Issue#getWatchers()
	 */
    Watchers getWatchers(URI watchersUri, ProgressMonitor progressMonitor);

	/**
	 * Retrieves complete information (if the caller has permission) about voters for selected issue.
	 *
	 * @param votesUri URI of voters resource for selected issue. Usually obtained by calling <code>Issue.getVotesUri()</code>
	 * @param progressMonitor progress monitor  
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)

	 * @return detailed information about voters of selected issue
	 * @see com.atlassian.jira.rest.client.domain.Issue#getVotesUri()
	 */
	Votes getVotes(URI votesUri, ProgressMonitor progressMonitor);

	/**
	 * Retrieves complete information (if the caller has permission) about transitions available for the selected issue in its current state.
	 *
	 * @param transitionsUri URI of transitions resource of selected issue. Usually obtained by calling <code>Issue.getTransitionsUri()</code>
	 * @param progressMonitor progress monitor  
	 * @return transitions about transitions available for the selected issue in its current state.
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	Iterable<Transition> getTransitions(URI transitionsUri, ProgressMonitor progressMonitor);
	
	
	
	/**
	 * Retrieves complete information (if the caller has permission) about versions for given project
	 *
	 * @param versionsUri URI of transitions resource of selected issue. Usually obtained by calling <code>Issue.getTransitionsUri()</code>
	 * @param progressMonitor progress monitor  
	 * @return transitions about transitions available for the selected issue in its current state.
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	Iterable<Version> getVersions(String projectKey, ProgressMonitor progressMonitor);

	/**
	 * Retrieves complete information (if the caller has permission) about transitions available for the selected issue in its current state.
	 *
	 * @since v0.5
	 * @param issue issue
	 * @param progressMonitor progress monitor
	 * @return transitions about transitions available for the selected issue in its current state.
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	Iterable<Transition> getTransitions(Issue issue, ProgressMonitor progressMonitor);

	/**
	 * Performs selected transition on selected issue.
	 * @param transitionsUri URI of transitions resource of selected issue. Usually obtained by calling <code>Issue.getTransitionsUri()</code>
	 * @param transitionInput data for this transition (fields modified, the comment, etc.)
	 * @param progressMonitor progress monitor  
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)

	 */
	void transition(URI transitionsUri, TransitionInput transitionInput, ProgressMonitor progressMonitor);

	/**
	 * Performs selected transition on selected issue.
	 * @since v0.5
	 * @param issue selected issue
	 * @param transitionInput data for this transition (fields modified, the comment, etc.)
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)

	 */
	void transition(Issue issue, TransitionInput transitionInput, ProgressMonitor progressMonitor);

	/**
	 * Casts your vote on the selected issue. Casting a vote on already votes issue by the caller, causes the exception.
	 * @param votesUri URI of votes resource for selected issue. Usually obtained by calling <code>Issue.getVotesUri()</code>
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	void vote(URI votesUri, ProgressMonitor progressMonitor);

	/**
	 * Removes your vote from the selected issue. Removing a vote from the issue without your vote causes the exception.
	 * @param votesUri URI of votes resource for selected issue. Usually obtained by calling <code>Issue.getVotesUri()</code>
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	void unvote(URI votesUri, ProgressMonitor progressMonitor);

	/**
	 * Starts watching selected issue
	 * @param watchersUri
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	void watch(URI watchersUri, ProgressMonitor progressMonitor);

	/**
	 * Stops watching selected issue
	 * @param watchersUri
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	void unwatch(URI watchersUri, ProgressMonitor progressMonitor);

	/**
	 * Adds selected person as a watcher for selected issue. You need to have permissions to do that (otherwise
	 * the exception is thrown).
	 *
	 * @param watchersUri
	 * @param username user to add as a watcher
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	void addWatcher(final URI watchersUri, final String username, ProgressMonitor progressMonitor);

	/**
	 * Removes selected person from the watchers list for selected issue. You need to have permissions to do that (otherwise
	 * the exception is thrown).
	 *
	 * @param watchersUri
	 * @param username user to remove from the watcher list
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, etc.)
	 */
	void removeWatcher(final URI watchersUri, final String username, ProgressMonitor progressMonitor);

	/**
	 * Creates link between two issues and adds a comment (optional) to the source issues.
	 *
	 * @param linkIssuesInput details for the link and the comment (optional) to be created
	 * @param progressMonitor progress monitor
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid argument, permissions, etc.)
	 * @since client 0.2, server 4.3
	 */
	void linkIssue(LinkIssuesInput linkIssuesInput, ProgressMonitor progressMonitor);

	/**
	 * Uploads attachments to JIRA (adding it to selected issue)
	 *
	 * @param progressMonitor progress monitor
	 * @param attachmentsUri where to upload the attachment. You can get this URI by examining issue resource first
	 * @param in stream from which to read data to upload
	 * @param filename file name to use for the uploaded attachment
	 * @since client 0.2, server 4.3
	 */
	void addAttachment(ProgressMonitor progressMonitor, URI attachmentsUri, InputStream in, String filename);

	/**
	 * Uploads attachments to JIRA (adding it to selected issue)
	 *
	 * @param progressMonitor progress monitor
	 * @param attachmentsUri where to upload the attachments. You can get this URI by examining issue resource first
	 * @param attachments attachments to upload
	 * @since client 0.2, server 4.3
	 */
	void addAttachments(ProgressMonitor progressMonitor, URI attachmentsUri, AttachmentInput ... attachments);

	/**
	 * Uploads attachments to JIRA (adding it to selected issue)
	 * @param progressMonitor progress monitor
	 * @param attachmentsUri where to upload the attachments. You can get this URI by examining issue resource first
	 * @param files files to upload
	 * @since client 0.2, server 4.3
	 */
	void addAttachments(ProgressMonitor progressMonitor, URI attachmentsUri, File... files);

	/**
	 * Retrieves the content of given attachment.
	 *
	 *
	 * @param pm progress monitor
	 * @param attachmentUri URI for the attachment to retrieve
	 * @return stream from which the caller may read the attachment content (bytes). The caller is responsible for closing the stream.
	 */
	@Beta
	public InputStream getAttachment(ProgressMonitor pm, URI attachmentUri);

	/**
	 * Expandos supported by {@link IssueRestClient#getIssue(String, Iterable, ProgressMonitor)}
	 */
	public enum Expandos {
		CHANGELOG, SCHEMA, NAMES, TRANSITIONS
	}

	/**
	 * creates new issue
	 * @param progressMonitor
	 * @param issueJson
	 * @return IssueID
	 */
	String createIssue(ProgressMonitor progressMonitor, String issueJson);

	/**
	 * Updates assignee
	 * @param progressMonitor
	 * @param issueJson
	 * @param issueID
	 */
	void updateAssigee(ProgressMonitor progressMonitor, String issueJson, String issueID);
}
