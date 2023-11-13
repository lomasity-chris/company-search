package org.trunarrative.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trunarrative.domain.CompaniesOutputDTO;

import java.util.Optional;

@Slf4j
@Component
public class DatabaseAdapter {

    @Autowired
    CompanySearchRepository companySearchRepository;

    public CompaniesOutputDTO read(String companyNumber, String onlyActive) {

        CompaniesOutputDTO companiesOutputDTO = null;

        CompaniesEntity.CompaniesId companiesId = CompaniesEntity.CompaniesId.builder().companyNumber(companyNumber).onlyActive(onlyActive).build();
        Optional<CompaniesEntity> byCompanyNumberAndOnlyActive = companySearchRepository.findById(companiesId);
        if (byCompanyNumberAndOnlyActive.isPresent()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                companiesOutputDTO = objectMapper.readValue(byCompanyNumberAndOnlyActive.get().getCompaniesOutput(), CompaniesOutputDTO.class);
            } catch (JsonProcessingException e) {
                log.warn("Error processing response from database so going to api. " + e.getMessage());
            }
        }

        return companiesOutputDTO;
    }

    public void save(String companyNumber, String onlyActive, CompaniesOutputDTO companiesOutput) {

        try {

            CompaniesEntity.CompaniesId companiesId = CompaniesEntity.CompaniesId.builder().companyNumber(companyNumber).onlyActive(onlyActive).build();
            ObjectMapper objectMapper = new ObjectMapper();
            String companiesOutputJSON = objectMapper.writeValueAsString(companiesOutput);

            CompaniesEntity companiesEntity = CompaniesEntity.builder()
                    .companiesId(companiesId)
                    .companiesOutput(companiesOutputJSON).build();
            companySearchRepository.save(companiesEntity);

        } catch (JsonProcessingException e) {
            log.warn("Unable to save response to database" + e.getMessage());
        }
    }
}
