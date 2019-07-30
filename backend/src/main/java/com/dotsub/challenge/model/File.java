package com.dotsub.challenge.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;


/**
 * File
 */
@Entity

public class File {

    @Id
    @GeneratedValue()
    private Long id;
    @NotEmpty
    private String description;
    @NotEmpty
    private String title;
    @CreationTimestamp
    private ZonedDateTime dateCreated;
    @NotEmpty
    @JsonIgnore
    private String dataUri;

    private String originalFileName;
    private String mimeType;

    public File() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDataUri() {
        return this.dataUri;
    }

    public void setDataUri(String dataUri) {
        this.dataUri = dataUri;
    }


	public String getOriginalFileName() {
		return originalFileName;
	}


	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}


	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof File)) {
            return false;
        }
        File file = (File) o;
        return Objects.equals(id, file.id) && Objects.equals(description, file.description) && Objects.equals(title, file.title) && Objects.equals(dateCreated, file.dateCreated) && Objects.equals(dataUri, file.dataUri) && Objects.equals(originalFileName, file.originalFileName) && Objects.equals(mimeType, file.mimeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, title, dateCreated, dataUri, originalFileName, mimeType);
    }


}