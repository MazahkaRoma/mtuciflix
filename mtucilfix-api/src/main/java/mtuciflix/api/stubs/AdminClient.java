package mtuciflix.api.stubs;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import mtuciflix.api.*;

import java.io.InputStream;
import java.nio.file.*;

public class AdminClient {
    private final AdminToolGrpc.AdminToolBlockingStub adminStub;
    private final FileServiceGrpc.FileServiceStub fileServiceStub;
    private final ManagedChannel channel;

    public AdminClient(String server, int port) {
        channel = ManagedChannelBuilder
                .forAddress(server, port)
                .usePlaintext()
                .build();
        adminStub = AdminToolGrpc.newBlockingStub(channel);
        fileServiceStub = FileServiceGrpc.newStub(channel);
    }

    public boolean insertMedia(int mediaId,
                               int mediaImgPreviewId,
                               int mediaFullscreenPreviewId,
                               String mediaTitle,
                               String director,
                               String mediaDescription,
                               boolean isAvailable) {

        WriteMediaInfoRequest request = WriteMediaInfoRequest
                .newBuilder()
                .setMediaId(mediaId)
                .setMediaFullscreenPreviewId(mediaFullscreenPreviewId)
                .setMediaImgPreviewId(mediaImgPreviewId)
                .setMediaTitle(mediaTitle)
                .setDirector(director)
                .setMediaDescription(mediaDescription)
                .setIsAvailable(isAvailable)
                .build();

        return adminStub.writeMediaInfo(request).getResult();

    }

    static class FileUploadObserver implements StreamObserver<FileUploadResponse> {

        @Override
        public void onNext(FileUploadResponse fileUploadResponse) {
            System.out.println(
                    "File upload status :: " + fileUploadResponse.getStatus()
            );
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onCompleted() {
            System.out.println("File has been uploaded!!!)))");
        }

    }

    public void upload(String filePath, String type, String title) {
        StreamObserver<FileUploadRequest> streamObserver = this.fileServiceStub.upload(new FileUploadObserver());
        Path path = Paths.get(filePath);


        FileUploadRequest metadata = FileUploadRequest.newBuilder()
                .setMetadata(MetaData.newBuilder()
                        .setName(title)
                        .setType(type)
                        .build())
                .build();
        streamObserver.onNext(metadata);

        try {
            InputStream inputStream = Files.newInputStream(path);
            byte[] bytes = new byte[2_097_152];
            int size;
            while ((size = inputStream.read(bytes)) > 0) {
                FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
                        .setFile(File.newBuilder().setContent(ByteString.copyFrom(bytes, 0, size)).build())
                        .build();
                Thread.sleep(50);
                streamObserver.onNext(uploadRequest);
            }
            inputStream.close();
            streamObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        channel.shutdown();
    }

}
