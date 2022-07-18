package br.com.lkm.extrator.dto.outlook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveMailFolderRequest {

    private String destinationId;

    public MoveMailFolderRequest(String destinationFolderId) {
        this.destinationId = destinationFolderId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

}
