package org.trunarrative;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import org.trunarrative.database.CompanySearchRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8081)
public class CompanySearchControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanySearchRepository repository;

    static String companiesResponse;
    static String officersResponse;
    static String expectedResult;

    private static final String X_API_KEY = "gsegwauygvuyeguivqcguiyvqegr";

    @BeforeAll
    static void setupBeforeAll() throws IOException {
        companiesResponse = FileUtils.readFileToString(ResourceUtils.getFile("classpath:companies-response.json"), StandardCharsets.UTF_8);
        officersResponse = FileUtils.readFileToString(ResourceUtils.getFile("classpath:officers-response.json"), StandardCharsets.UTF_8);
        expectedResult = FileUtils.readFileToString(ResourceUtils.getFile("classpath:expected-result.json"), StandardCharsets.UTF_8);
    }

    @BeforeEach
    void setupBeforeEach() {
        repository.deleteAll();

        stubFor(get(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search*"))
                .withHeader("x-api-key", equalTo(X_API_KEY))
                .withQueryParam("Query", equalTo("06500244"))
                .willReturn(jsonResponse(companiesResponse, 200)));
        stubFor(get(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Officers*"))
                .withHeader("x-api-key", equalTo(X_API_KEY))
                .withQueryParam("CompanyNumber", equalTo("06500244"))
                .willReturn(jsonResponse(officersResponse, 200)));
    }

    @Test
    void whenXApiKeyIsNotPassed_then400Error() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/search")).andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void whenValidRequest_thenExpectedResultIsReturned() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .header("x-api-key", X_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\":\"BBC\", \"companyNumber\":\"06500244\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));

        verify(exactly(1), getRequestedFor(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search")));
        verify(exactly(1), getRequestedFor(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Officers")));
    }

    @Test
    void givenCompanyHasBeenRetrievedAndStoredOnDatabase_whenValidRequest_thenResultIsRetrievedFromDatabase() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .header("x-api-key", X_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\":\"BBC\", \"companyNumber\":\"06500244\"}"))
                .andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .header("x-api-key", X_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\":\"BBC\", \"companyNumber\":\"06500244\"}"))
                .andExpect(status().isOk());

        verify(exactly(1), getRequestedFor(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search")));
        verify(exactly(1), getRequestedFor(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Officers")));
    }

    @Test
    void givenActiveOnlyHasBeenRetrievedAndStoredOnDatabase_whenValidRequestForAllCompanies_thenResultIsRetrievedFromApi() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .header("x-api-key", X_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\":\"BBC\", \"companyNumber\":\"06500244\"}")
                        .param("onlyActive", "Y"))
                .andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .header("x-api-key", X_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\":\"BBC\", \"companyNumber\":\"06500244\"}"))
                .andExpect(status().isOk());

        verify(exactly(2), getRequestedFor(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search")));
        verify(exactly(2), getRequestedFor(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Officers")));
    }
}
