package org.trunarrative.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficersInputDTO {

    @JsonProperty("items")
    @Getter
    private List<Officer> officerList;

    @Builder
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
        @JsonProperty("resigned_on")
        @Getter
        private String resignedOn;
        @JsonProperty
        @Getter
        private AddressInputDTO address;
    }
}

