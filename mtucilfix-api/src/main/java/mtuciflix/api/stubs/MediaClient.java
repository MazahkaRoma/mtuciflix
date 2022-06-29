package mtuciflix.api.stubs;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import mtuciflix.api.*;
import mtuciflix.api.utils.MediaInfo;

import java.util.HashMap;
import java.util.Map;

public class MediaClient {
    private final MultiMediaInfoProviderGrpc.MultiMediaInfoProviderBlockingStub stubForMedia;
    private final UserUtilsGrpc.UserUtilsBlockingStub stubForUser;
    private final ManagedChannel channel;
    private int clientId;
    private static final Map<Integer, String> USER_RIGHTS = new HashMap<>();

    static {
        USER_RIGHTS.put(0, "NONE");
        USER_RIGHTS.put(1, "ALL");
        USER_RIGHTS.put(2, "ONLY_VIDEO");
        USER_RIGHTS.put(3, "ONLY_MUSIC");
    }

    public MediaClient(String server, int port) {
        channel = ManagedChannelBuilder
                .forAddress(server, port)
                .usePlaintext()
                .build();
        stubForMedia = MultiMediaInfoProviderGrpc.newBlockingStub(channel);
        stubForUser = UserUtilsGrpc.newBlockingStub(channel);
    }

    public Map<Integer, Long> getListOfMedia() {
        GetMediaChunkResponse response = stubForMedia.getListOfAvailableMedia(GetMediaUtilRequest
                .newBuilder()
                .setClientId(clientId)
                .setMediaId(0)
                .build());

        return response.getMediaChunkMap();
    }

    public boolean auth(String login, String password) {
        CheckUserLogInResponse response = stubForUser.login(CheckUserLogInRequest
                .newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build());

        this.clientId = response.getClientId();
        return response.getIsLogin();
    }

    public boolean register(String login, String password) {
        CheckUserLogInResponse response = stubForUser.register(CheckUserLogInRequest
                .newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build());

        this.clientId = response.getClientId();
        return response.getIsLogin();
    }

    public MediaInfo getMediaInfo(int mediaId) {
        GetMediaInfoResponse response = stubForMedia.getMediaInfo(GetMediaUtilRequest.newBuilder()
                .setMediaId(mediaId)
                .setClientId(clientId)
                .build());

        return new MediaInfo(response.getTitle(), response.getDescription(), response.getDirector(), response.getMediaId());
    }

    private boolean isRightRights() {
        int rights = stubForUser.checkRight(CheckRightsRequest.newBuilder()
                        .setClientId(clientId)
                        .build())
                .getRights();

        return USER_RIGHTS.containsKey(rights);
    }

    public void shutdown() {
        channel.shutdown();
    }

}
