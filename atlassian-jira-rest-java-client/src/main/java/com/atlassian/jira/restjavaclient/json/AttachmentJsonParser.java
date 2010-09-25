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

package com.atlassian.jira.restjavaclient.json;

import com.atlassian.jira.restjavaclient.domain.Attachment;
import com.atlassian.jira.restjavaclient.domain.BasicUser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;

import java.net.URI;

/**
 * TODO: Document this class / interface here
 *
 * @since v0.1
 */
public class AttachmentJsonParser implements JsonParser<Attachment> {

	private static final String THUMBNAIL = "thumbnail";

	@Override
	public Attachment parse(JSONObject json) throws JSONException {
		final URI selfUri = JsonParseUtil.getSelfUri(json);
		final String filename = json.getString("filename");
		final BasicUser author = JsonParseUtil.parseUser(json.getJSONObject("author"));
		final DateTime creationDate = JsonParseUtil.parseDateTime(json.getString("created"));
		final int size = json.getInt("size");
		final String mimeType = json.getString("mimeType");
		final URI contentURI = JsonParseUtil.parseURI(json.getString("content"));
		final URI thumbnailURI = JsonParseUtil.parseOptionalURI(json, THUMBNAIL);
		return new Attachment(selfUri, filename, author, creationDate, size, mimeType, contentURI, thumbnailURI);
	}
}