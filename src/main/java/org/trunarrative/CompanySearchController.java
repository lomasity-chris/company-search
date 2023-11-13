package org.trunarrative;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.trunarrative.domain.CompaniesOutputDTO;
import org.trunarrative.domain.SearchQuery;

@RestController
public class CompanySearchController {

    @Autowired
    CompanySearchService companySearchService;

    @GetMapping("/api/search")
    CompaniesOutputDTO search(@RequestHeader("x-api-key") String xApiKey, @RequestBody SearchQuery searchQuery,
                              @RequestParam(defaultValue = "N") String onlyActive) {

        CompaniesOutputDTO companiesOutputDTO;
        if (!StringUtils.isEmpty(searchQuery.getCompanyNumber())) {
            companiesOutputDTO = companySearchService.findByCompanyNumber(xApiKey, searchQuery.getCompanyNumber(), onlyActive);
        } else {
            companiesOutputDTO = companySearchService.findByCompanyName(xApiKey, searchQuery.getCompanyName(), onlyActive);
        }
        return companiesOutputDTO;
    }
}
