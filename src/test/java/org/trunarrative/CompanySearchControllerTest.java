package org.trunarrative;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trunarrative.domain.SearchQuery;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CompanySearchControllerTest {

    @Mock
    CompanySearchService companySearchService;
    @InjectMocks
    CompanySearchController companySearchController;

    private static final String X_API_KEY = "apiKey";

    @Test
    void whenCompanyNumberIsPassed_thenCompanyNumberIsUsedToSearch() {

        SearchQuery searchQuery = SearchQuery.builder().companyNumber("1234").companyName("abc").build();
        companySearchController.search(X_API_KEY, searchQuery, "Y");
        verify(companySearchService, times(1)).findByCompanyNumber(X_API_KEY, "1234", "Y");
    }

    @Test
    void whenCompanyNumberIsNotPassed_thenCompanyNameIsUsedToSearch() {

        SearchQuery searchQuery = SearchQuery.builder().companyName("abc").build();
        companySearchController.search(X_API_KEY, searchQuery, "Y");
        verify(companySearchService, times(1)).findByCompanyName(X_API_KEY, "abc", "Y");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Y", "N", ""})
    void onlyActiveInputParamIsUsed(String onlyActive) {

        SearchQuery searchQuery = SearchQuery.builder().companyName("abc").build();
        companySearchController.search(X_API_KEY, searchQuery, onlyActive);
        verify(companySearchService, times(1)).findByCompanyName(X_API_KEY, "abc", onlyActive);
    }
}
