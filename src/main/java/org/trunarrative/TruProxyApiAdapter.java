package org.trunarrative;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.trunarrative.domain.CompaniesInputDTO;
import org.trunarrative.domain.OfficersInputDTO;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TruProxyApiAdapter {

    private final RestTemplate restTemplate;

    @Value("${truProxyHost}")
    private String truProxyHost;

    @Autowired
    public TruProxyApiAdapter(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public CompaniesInputDTO getCompanies(String xApiKey, String searchTerm) {

        log.info("Calling /TruProxyAPI/rest/Companies/v1/Search");

        Map<String, String> params = new HashMap<>();
        params.put("search_term", searchTerm);

        ResponseEntity<CompaniesInputDTO> response = restTemplate.exchange(
                truProxyHost + "/TruProxyAPI/rest/Companies/v1/Search?Query={search_term}",
                HttpMethod.GET, requestEntity(xApiKey), CompaniesInputDTO.class, params);

        return response.getBody();
    }

    public OfficersInputDTO getOfficers(String xApiKey, String companyNumber) {

        log.info("Calling /TruProxyAPI/rest/Companies/v1/Officers");

        Map<String, String> params = new HashMap<>();
        params.put("number", companyNumber);

        ResponseEntity<OfficersInputDTO> response = restTemplate.exchange(
                truProxyHost + "/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber={number}",
                HttpMethod.GET, requestEntity(xApiKey), OfficersInputDTO.class, params);

        return response.getBody();
    }

    private HttpEntity<Void> requestEntity(String xApiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", xApiKey);
        return new HttpEntity<>(headers);
    }
}
