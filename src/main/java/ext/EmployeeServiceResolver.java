package ext;


import api.*;
import model.api.UserInfo;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;

public class EmployeeServiceResolver implements ParameterResolver {
    private final static String DEFAULT_USER = "donatello";
    private final static String DEFAULT_PASS = "does-machines";
    public static final String URL = "https://x-clients-be.onrender.com";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(EmployeeService.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new LogInterceptor()).build();
        EmployeeService service = new EmployeeServiceImpl(client, URL);

        if (parameterContext.isAnnotated(Authorized.class)) {
            Authorized auth = parameterContext.getParameter().getAnnotation(Authorized.class);
            AuthorizationService authorizeService = new AuthorizationServiceImpl(client, URL);
            UserInfo userInfo;
            try {
                if (!auth.username().isBlank()) {
                    userInfo = authorizeService.auth(auth.username(), auth.password());
                } else {
                    userInfo = authorizeService.auth(DEFAULT_USER, DEFAULT_PASS);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            service.setToken(userInfo.getUserToken());
        }

        return service;
    }

}
