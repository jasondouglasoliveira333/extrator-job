package br.com.lkm.extrator.service;

import br.com.lkm.extrator.dto.outlook.Attachment;
import br.com.lkm.extrator.dto.outlook.CreateFolderRequest;
import br.com.lkm.extrator.dto.outlook.Folder;
import br.com.lkm.extrator.dto.outlook.Message;
import br.com.lkm.extrator.dto.outlook.MoveMailFolderRequest;
import br.com.lkm.extrator.dto.outlook.OutlookUser;
import br.com.lkm.extrator.dto.outlook.PagedResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OutlookService {

    @GET("/v1.0/me")
    Call<OutlookUser> getCurrentUser();

    @GET("/v1.0/me/mailfolders/{folderid}/messages")
    Call<PagedResult<Message>> getMessages(
            @Path("folderid") String folderId,
            @Query("$orderby") String orderBy,
            @Query("$select") String select,
            @Query("$top") Integer maxResults
    );

    @GET("/v1.0/me/messages/{message_id}/attachments")
    Call<PagedResult<Attachment>> getAttachments(
            @Path("message_id") String messagId
    );

    @POST("/v1.0/me/mailFolders/{folder_id}/childFolders/")
    Call<Folder> createFolder(
            @Path("folder_id") String folderId,
            @Body CreateFolderRequest folderRequest
    );

    @POST("/v1.0/me/mailFolders")
    Call<Folder> createFolder(
            @Body CreateFolderRequest folderRequest
    );

    // Só esta funcionando quando buscamos por "inbox"
    @GET("/v1.0/me/mailFolders/{folder_id}")
    Call<Folder> getFolder(
            @Path("folder_id") String folderId
    );

    @GET("/v1.0/me/mailFolders")
    Call<PagedResult<Folder>> getFolders();

    @POST("/v1.0/me/messages/{message_id}/move")
    Call<Folder> moveMailFolder(
            @Path("message_id") String messageId,
            @Body MoveMailFolderRequest moveMailFolderRequest
    );
}
