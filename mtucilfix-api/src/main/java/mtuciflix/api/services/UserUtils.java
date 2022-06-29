package mtuciflix.api.services;

import io.grpc.stub.StreamObserver;
import mtuciflix.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;

public class UserUtils extends UserUtilsGrpc.UserUtilsImplBase {
    private Connection connection;
    private static final Logger LOG = LoggerFactory.getLogger(UserUtils.class);

    public UserUtils(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void login(CheckUserLogInRequest request,
                      StreamObserver<CheckUserLogInResponse> responseObserver) {

        String login = request.getLogin();
        String password = request.getPassword();
        CheckUserLogInResponse.Builder responseBuilder = CheckUserLogInResponse.newBuilder();

        try (ResultSet cursorForId = connection.prepareStatement("SELECT user_id FROM users where user_login='" + login + "';").executeQuery()) {
            if (cursorForId.next()) {
                int id = cursorForId.getInt("user_id");

                try (ResultSet cursorForPassword = connection.prepareStatement("SELECT user_id FROM users_access WHERE user_password='" + password + "';").executeQuery()) {
                    if (cursorForPassword.next()) {
                        int id_check = cursorForPassword.getInt("user_id");
                        responseBuilder.setIsLogin(id_check == id)
                                .setClientId(id);
                    } else {
                        responseBuilder.setClientId(0)
                                .setIsLogin(false);
                    }

                }

            } else {
                responseBuilder.setIsLogin(false)
                        .setClientId(0);
            }
            responseObserver.onNext(responseBuilder.build());
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void checkRight(CheckRightsRequest request,
                           StreamObserver<CheckRightsResponse> responseObserver) {
        int clientId = request.getClientId();
        CheckRightsResponse response;
        try (ResultSet cursor = connection.prepareStatement("SELECT client_right FROM client_rights where client_id=" + clientId + ";").executeQuery()) {
            if (cursor.next()) {
                response = CheckRightsResponse.newBuilder()
                        .setClientId(clientId)
                        .setRights(cursor.getInt("client_right"))
                        .build();
                responseObserver.onNext(response);
            }
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void register(CheckUserLogInRequest request,
                         StreamObserver<CheckUserLogInResponse> responseObserver) {
        String login = request.getLogin();
        String password = request.getPassword();

        try {
            int rows_for_login = connection.prepareStatement("INSERT INTO users(user_login) VALUES('" + login + "')").executeUpdate();
            if (rows_for_login == 0) {
                responseObserver.onNext(CheckUserLogInResponse.newBuilder()
                        .setClientId(rows_for_login)
                        .setIsLogin(false)
                        .build()
                );
                throw new Exception("Not able to register user");
            }
            ResultSet cursor = connection.prepareStatement("SELECT user_id FROM users WHERE user_login='" + login + "';").executeQuery();
            if (cursor.next()) {
                int id = cursor.getInt("user_id");
                int res = connection.prepareStatement("INSERT INTO users_access(user_id,user_password) VALUES(" + id + ",'" + password + "')").executeUpdate();
                if (res == 0) {
                    responseObserver.onNext(CheckUserLogInResponse.newBuilder()
                            .setClientId(rows_for_login)
                            .setIsLogin(false)
                            .build()
                    );
                    throw new Exception("Not able to register user");
                } else {
                    responseObserver.onNext(CheckUserLogInResponse.newBuilder()
                            .setClientId(id)
                            .setIsLogin(true)
                            .build());
                }
            }
        } catch (Throwable e) {

        }
        responseObserver.onCompleted();
    }
}
