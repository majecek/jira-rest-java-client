package com.atlassian.jira.restjavaclient.domain;

import com.atlassian.jira.restjavaclient.AddressableEntity;
import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.net.URI;

/**
 * TODO: Document this class / interface here
 *
 * @since v4.2
 */
public class Attachment implements AddressableEntity {
    private final URI self;
    private final String filename;
    private final User author;
    private final DateTime creationDate;
    private final int size;
    private final String mimeType;
    private final URI contentUri;

    @Nullable
    private final URI thumbnailUri;

    public Attachment(URI self, String filename, User author, DateTime creationDate, int size, String mimeType, URI contentUri, URI thumbnailUri) {
        this.self = self;
        this.filename = filename;
        this.author = author;
        this.creationDate = creationDate;
        this.size = size;
        this.mimeType = mimeType;
        this.contentUri = contentUri;
        this.thumbnailUri = thumbnailUri;
    }

    public boolean hasThumbnail() {
        return thumbnailUri != null;
    }

    public URI getSelf() {
        return null;
    }

    public String getFilename() {
        return filename;
    }

    public User getAuthor() {
        return author;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public int getSize() {
        return size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public URI getContentUri() {
        return contentUri;
    }

    @Nullable
    public URI getThumbnailUri() {
        return thumbnailUri;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("self", self).
                add("filename", filename).
                add("author", author).
                add("creationDate", creationDate).
                add("size", size).
                add("mimeType", mimeType).
                add("contentUri", contentUri).
                add("thumbnailUri", thumbnailUri).
                toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attachment) {
            Attachment that = (Attachment) obj;
            return Objects.equal(this.self, that.self)
                    && Objects.equal(this.filename, that.filename)
                    && Objects.equal(this.author, that.author)
                    && Objects.equal(this.creationDate, that.creationDate)
                    && Objects.equal(this.size, that.size)
                    && Objects.equal(this.mimeType, that.mimeType)
                    && Objects.equal(this.contentUri, that.contentUri)
                    && Objects.equal(this.thumbnailUri, that.thumbnailUri);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(self, filename, author, creationDate, size, mimeType, contentUri, thumbnailUri);
    }
}
