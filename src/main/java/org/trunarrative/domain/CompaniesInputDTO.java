package org.trunarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesInputDTO {

    @JsonProperty("total_results")
    private int totalResults;

    @JsonProperty("items")
    @Getter
    private List<Company> companyList;

    @Builder
    public static class Company {
        @JsonProperty("company_number")
        @Getter
        private String companyNumber;
        @JsonProperty("company_type")
        @Getter
        private String companyType;
        @JsonProperty
        @Getter
        private String title;
        @JsonProperty("company_status")
        @Getter
        @Setter
        private String companyStatus;
        @JsonProperty("date_of_creation")
        @Getter
        private String dateOfCreation;
        @JsonProperty
        @Getter
        private AddressInputDTO address;
    }
}

