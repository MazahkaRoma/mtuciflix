package mtuciflix.api.services;

import io.grpc.stub.StreamObserver;
import mtuciflix.api.AdminToolGrpc;
import mtuciflix.api.WriteMediaInfoRequest;
import mtuciflix.api.WriteMediaInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminToolImpl extends AdminToolGrpc.AdminToolImplBase {
    private Connection connection;

    private static final Logger LOG = LoggerFactory.getLogger(AdminToolImpl.class);

    public AdminToolImpl(Connection connection) {
        super();
        this.connection = connection;
        System.out.println();
    }

    @Override
    public void writeMediaInfo(WriteMediaInfoRequest request,
                               StreamObserver<WriteMediaInfoResponse> responseObserver) {

        int mediaId = request.getMediaId();
        int mediaImgPreviewId = request.getMediaImgPreviewId();
        int mediaFullscreenPreviewId = request.getMediaFullscreenPreviewId();
        String mediaTitle = request.getMediaTitle();
        String director = request.getDirector();
        int isAvailable = request.getIsAvailable() ? 1 : 0;
        String mediaDescription = request.getMediaDescription();
        WriteMediaInfoResponse response;
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO media (media_id, media_img_preview, is_avaliable, media_vid_id) VALUES (?,?,?,?);");
            statement.setInt(1, mediaId);
            statement.setInt(2, mediaImgPreviewId);
            statement.setInt(3,  isAvailable);
            statement.setInt(4, mediaFullscreenPreviewId);
            statement.executeUpdate();
            PreparedStatement insertIntoMediaInfo = connection
                    .prepareStatement("INSERT INTO media_info (media_id, title, description, director) VALUES (?, ?, ?, ?);");
            insertIntoMediaInfo.setInt(1, mediaId);
            insertIntoMediaInfo.setString(2, mediaTitle);
            insertIntoMediaInfo.setString(3, mediaDescription);
            insertIntoMediaInfo.setString(4, director);
            insertIntoMediaInfo.executeUpdate();

            response = WriteMediaInfoResponse.newBuilder()
                    .setResult(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            LOG.error("Error accrued during insert in to db", e);
        }

    }
}
