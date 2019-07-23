package com.dotsub.challenge.dto;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * FileDTO
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FileDTO {
    
    @Min(1)
    private Long id;
    @NotEmpty
    private String description;
    @NotEmpty
    private String title;
    @NotNull
    private byte[] data;


    public FileDTO() {
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

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FileDTO)) {
            return false;
        }
        FileDTO fileDTO = (FileDTO) o;
        return Objects.equals(id, fileDTO.id) && Objects.equals(description, fileDTO.description) && Objects.equals(title, fileDTO.title) && Objects.equals(data, fileDTO.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, title, data);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", description='" + getDescription() + "'" +
            ", title='" + getTitle() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }

}