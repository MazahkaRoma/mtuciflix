package org.mtuciflix.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import mtuciflix.api.services.AdminToolImpl;
import mtuciflix.api.services.MultiMediaProviderImpl;
import mtuciflix.api.services.UploadFileImpl;
import mtuciflix.api.services.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.mtuciflix.server.PropKeys.*;

public class ServerExec {

    private static Logger LOG = LoggerFactory.getLogger(ServerExec.class);
    private static Properties properties = new Properties();

    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            try (InputStream inputProp = new FileInputStream(args[0])) {
                if (inputProp == null) {
                    LOG.error("Not a config file");
                    return;
                }
                properties.load(inputProp);
            }
        } else {
            try (InputStream inputProp = ServerExec.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (inputProp == null) {
                    LOG.error("No config found");
                    return;
                }

                properties.load(inputProp);
            }
        }

        logConfig();
        String pathToJks = ServerExec.class.getClassLoader().getResource(properties.getProperty(JKS_NAME)).getPath();
        System.setProperty("javax.net.ssl.trustStore", pathToJks);
        System.setProperty("javax.net.ssl.trustStorePassword", properties.getProperty(JKS_PASSWORD));

        Connection mediaMetaProviderConn = DriverManager
                .getConnection(
                        properties.getProperty(META_MEDIA_URL),
                        properties.getProperty(META_MEDIA_USR),
                        properties.getProperty(META_MEDIA_PASS)
                );

        Connection userUtilsProviderConn = DriverManager
                .getConnection(
                        properties.getProperty(USER_UTILS_URL),
                        properties.getProperty(USER_UTILS_USR),
                        properties.getProperty(USER_UTILS_PASS)
                );

        Connection multiMediaProviderConn = DriverManager
                .getConnection(
                        properties.getProperty(VIDEO_PROVIDER_URL),
                        properties.getProperty(VIDEO_PROVIDER_USR),
                        properties.getProperty(VIDEO_PROVIDER_PASS)
                );

        Server server = ServerBuilder.forPort(555)
                .addService(new UploadFileImpl(properties.getProperty(MEDIA_STORAGE_PATH)))
                .addService(new UserUtils(userUtilsProviderConn))
                .addService(new AdminToolImpl(mediaMetaProviderConn))
                .addService(new MultiMediaProviderImpl(multiMediaProviderConn))
                .build();
        server.start();
        LOG.info("Server is running");
        server.awaitTermination();

    }

    private static void logConfig() {
        LOG.info("Running with config: {}", properties.toString());
    }
}
