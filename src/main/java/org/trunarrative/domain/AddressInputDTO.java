package org.trunarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
public class AddressInputDTO {

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
