package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.api.ApiError;
import model.api.ApiResponse;
import model.api.CreateCompanyRequest;
import model.api.CreateCompanyResponse;
import model.api.Company;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CompanyServiceImpl implements CompanyService{
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=UTF-8");
    private static final String PATH = "company";
    private final String BASE_PATH;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    private String token;

    public CompanyServiceImpl(OkHttpClient client, String url) {
        this.client = client;
        this.BASE_PATH = url;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @NotNull
    private HttpUrl.Builder getUrl() {
        return HttpUrl.parse(BASE_PATH).newBuilder().addPathSegment(PATH);
    }

    @Override
    public Company getById(int id) throws IOException {
        HttpUrl url = getUrl()
                .addPathSegment(Integer.toString(id))
                .build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        return mapper.readValue(response.body().string(), Company.class);
    }

    @Override
    public ApiResponse<CreateCompanyResponse> create(String name) throws IOException {
        return null;
    }

    @Override
    public ApiResponse<CreateCompanyResponse> create(String name, String description) throws IOException {
        HttpUrl url = getUrl().build();
        CreateCompanyRequest body = new CreateCompanyRequest(name, description);
        RequestBody jsonBody = RequestBody.create(mapper.writeValueAsString(body), APPLICATION_JSON);
        Request.Builder request = new Request.Builder().post(jsonBody).url(url);

        if (token != null) {
            request.addHeader("x-client-token", token);
        }

        Response response = this.client.newCall(request.build()).execute();
        if (response.code() >= 400) {
            ApiError body1 = mapper.readValue(response.body().string(), ApiError.class);
            return new ApiResponse<>(response.headers().toMultimap(), response.code(), null, body1);
        } else {
            CreateCompanyResponse body1 = mapper.readValue(response.body().string(), CreateCompanyResponse.class);
            return new ApiResponse<>(response.headers().toMultimap(), response.code(), body1, null);
        }
    }

    @Override
    public void deleteById(int id) {

    }
}
