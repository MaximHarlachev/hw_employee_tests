package api;

import model.api.ApiResponse;
import model.api.CreateCompanyResponse;
import model.api.Company;

import java.io.IOException;

public interface CompanyService extends Authorization{
    Company getById(int id) throws IOException;

    ApiResponse<CreateCompanyResponse> create(String name) throws IOException;

    ApiResponse<CreateCompanyResponse> create(String name, String description) throws IOException;

    void deleteById(int id);
}
