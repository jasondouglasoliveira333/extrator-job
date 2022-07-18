package br.com.lkm.extrator.dto.outlook;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {

    private String id;
    private String name;
    private String contentType;
    private BigInteger size;
    private boolean isInline;
    private Date lastModifiedDateTime;
    private String contentId;
    private String contentLocation;
    private boolean isContactPhoto;
    private byte[] contentBytes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public boolean isIsInline() {
        return isInline;
    }

    public void setIsInline(boolean isInline) {
        this.isInline = isInline;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public boolean isIsContactPhoto() {
        return isContactPhoto;
    }

    public void setIsContactPhoto(boolean isContactPhoto) {
        this.isContactPhoto = isContactPhoto;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

}
