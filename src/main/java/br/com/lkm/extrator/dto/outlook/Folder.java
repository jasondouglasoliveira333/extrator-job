package br.com.lkm.extrator.dto.outlook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Folder {

    private String id;
    private String displayName;
    private String parentFolderId;
    private int childFolderCount;
    private int unreadItemCount;
    private int totalItemCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getChildFolderCount() {
        return childFolderCount;
    }

    public void setChildFolderCount(int childFolderCount) {
        this.childFolderCount = childFolderCount;
    }

    public int getUnreadItemCount() {
        return unreadItemCount;
    }

    public void setUnreadItemCount(int unreadItemCount) {
        this.unreadItemCount = unreadItemCount;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

}
