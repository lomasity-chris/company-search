package org.trunarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"total_results", "items"})
public class CompaniesOutputDTO {

    @JsonProperty("total_results")
    @Getter
    private int totalResults;

    @JsonProperty("items")
    @Getter
    private List<Company> companyList;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"company_number", "company_type", "title", "company_status", "date_of_creation", "address", "officers"})
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
        private String companyStatus;
        @JsonProperty("date_of_creation")
        @Getter
        private String dateOfCreation;
        @JsonProperty
        @Getter
        private Address address;
        @JsonProperty
        @Getter
        @Setter
        private List<Officer> officers;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"locality", "postal_code", "premises", "address_line_1", "country"})
    public static class Address {
        @JsonProperty
        @Getter
        protected String locality;
        @JsonProperty("postal_code")
        @Getter
        private String postalCode;
        @JsonProperty
        @Getter
        private String premises;
        @JsonProperty("address_line_1")
        @Getter
        private String addressLine1;
        @JsonProperty
        @Getter
        private String country;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"name", "officer_role", "appointed_on", "address"})
    public static class Officer {

        @JsonProperty
        @Getter
        private String name;
        @JsonProperty("officer_role")
        @Getter
        private String officerRole;
        @JsonProperty("appointed_on")
        @Getter
        private String appointedOn;
        @JsonProperty
        @Getter
        private Address address;
    }
}

