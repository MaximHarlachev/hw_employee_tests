package api;

import model.api.UserInfo;

import java.io.IOException;

public interface AuthorizationService {
    UserInfo auth(String username, String password) throws IOException;
}
