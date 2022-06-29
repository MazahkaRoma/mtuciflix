package mtuciflix.api.services;

import io.grpc.stub.StreamObserver;
import mtuciflix.api.FileServiceGrpc;
import mtuciflix.api.FileUploadRequest;
import mtuciflix.api.FileUploadResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.protobuf.ByteString;
import mtuciflix.api.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.*;

public class UploadFileImpl extends FileServiceGrpc.FileServiceImplBase {
    private final Path SERVER_BASE_PATH;
    private final Logger LOG = LoggerFactory.getLogger(UploadFileImpl.class);
    private String type = "";

    public UploadFileImpl(@Nonnull String pathToOutput) {
        this.SERVER_BASE_PATH = Paths.get(pathToOutput);
    }


    @Override
    public StreamObserver<FileUploadRequest> upload(
            StreamObserver<FileUploadResponse> responseObserver) {
        return new StreamObserver<>() {
            OutputStream writer;
            Status status = Status.IN_PROGRESS;

            @Override
            public void onNext(FileUploadRequest fileUploadRequest) {
                try {
                    if (fileUploadRequest.hasMetadata()) {
                        type = fileUploadRequest.getMetadata().getType();
                        type = type.equals("jpg") ? type : "mp4";
                        writer = getFilePath(fileUploadRequest);
                    } else {
                        ByteString content = fileUploadRequest.getFile().getContent();
                        LOG.info("Writing byte batch:{}", content.size());
                        writeFile(writer, content);
                    }
                } catch (IOException e) {
                    this.onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                status = Status.FAILED;
                this.onCompleted();
            }

            @Override
            public void onCompleted() {
                closeFile(writer);
                status = Status.IN_PROGRESS.equals(status) ? Status.SUCCESS : status;
                LOG.info("Done with status {}", status);
                FileUploadResponse response = FileUploadResponse.newBuilder()
                        .setStatus(status)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private OutputStream getFilePath(FileUploadRequest request) throws IOException {
        var fileName = request.getMetadata().getName() + "." + type;
        return Files.newOutputStream(SERVER_BASE_PATH.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    private void writeFile(OutputStream writer, ByteString content) throws IOException {
        writer.write(content.toByteArray());
        writer.flush();
    }

    private void closeFile(OutputStream writer) {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}