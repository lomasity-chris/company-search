package org.trunarrative.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
public class SearchQuery {

    @Getter
    private String companyName;
    @Getter
    private String companyNumber;
}
