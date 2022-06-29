package mtuciflix.api.services;

import io.grpc.stub.StreamObserver;
import mtuciflix.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MultiMediaProviderImpl extends MultiMediaInfoProviderGrpc.MultiMediaInfoProviderImplBase {
    private final Connection dbConn;
    private final Logger LOG = LoggerFactory.getLogger(MultiMediaProviderImpl.class);

    public MultiMediaProviderImpl(Connection connection) {
        this.dbConn = connection;
    }

    @Override
    public void getListOfAvailableMedia(GetMediaUtilRequest request,
                                        StreamObserver<GetMediaChunkResponse> responseObserver) {
        int clientId = request.getClientId();
        Map<Integer, Long> medias = new HashMap<>();
        GetMediaChunkResponse response;

        try (ResultSet cursor = dbConn.prepareStatement("SELECT media_id, media_img_preview_id FROM media WHERE is_avaliable = 1;").executeQuery()) {
            while (cursor.next()) {
                int mediaId = cursor.getInt("media_id");
                long mediaImgPreview = cursor.getInt("media_img_preview_id");
                medias.put(mediaId, mediaImgPreview);
            }
        } catch (Exception e) {
            LOG.error("Exception accrued during parsing:", e);
        }

        response = GetMediaChunkResponse.newBuilder()
                .setClientId(clientId)
                .putAllMediaChunk(medias)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMediaInfo(GetMediaUtilRequest request,
                             StreamObserver<GetMediaInfoResponse> responseObserver) {
        int clientId = request.getClientId();
        long mediaId = request.getMediaId();
        GetMediaInfoResponse response;

        try (ResultSet cursor = dbConn.prepareStatement("SELECT title, description, director FROM media_info WHERE media_id=" + mediaId + ";").executeQuery()) {
            while (cursor.next()) {
                String title = cursor.getString("title");
                String description = cursor.getString("description");
                String director = cursor.getString("director");
                response = GetMediaInfoResponse.newBuilder()
                        .setMediaId((int) mediaId)
                        .setTitle(title)
                        .setDescription(description)
                        .setDirector(director)
                        .build();
                responseObserver.onNext(response);
            }
        } catch (Exception e) {
            LOG.error("Exception accrued during parsing:", e);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getMediaMetaInfo(GetMediaUtilRequest request,
                                 StreamObserver<GetMediaMetaInfoResponse> responseObserver) {

        int clientId = request.getClientId();
        long mediaId = request.getMediaId();
        GetMediaMetaInfoResponse response;
        try (ResultSet cursor = dbConn.prepareStatement("SELECT preview_fullscreen, media_length FROM media_meta_info WHERE media_id=" + mediaId + ";").executeQuery()) {
            int previewFullScreen = cursor.getInt("preview_fullscreen");
            String mediaLength = cursor.getString("media_length");
            response = GetMediaMetaInfoResponse.newBuilder()
                    .setMediaId((int) mediaId)
                    .setMediaMetaData(MediaMetaData.newBuilder()
                            .setDirector("none")
                            .setMediaId(mediaId)
                            .setMediaLength(mediaLength)
                            .setPreviewFullScreenId((long) previewFullScreen)
                            .build())
                    .build();
            responseObserver.onNext(response);
        } catch (Exception e) {
            LOG.error("Exception accrued during parsing:", e);
        }
        responseObserver.onCompleted();
    }

}
