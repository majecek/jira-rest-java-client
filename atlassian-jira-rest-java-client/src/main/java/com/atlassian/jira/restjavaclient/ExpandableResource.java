package com.atlassian.jira.restjavaclient;

/**
 * TODO: Document this class / interface here
 *
 * @since v0.1
 */
public interface ExpandableResource {
	Iterable<String> getExpandos();
}
