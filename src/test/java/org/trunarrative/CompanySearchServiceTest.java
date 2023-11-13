package org.trunarrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trunarrative.domain.AddressInputDTO;
import org.trunarrative.domain.CompaniesInputDTO;
import org.trunarrative.domain.CompaniesOutputDTO;
import org.trunarrative.domain.OfficersInputDTO;
import org.trunarrative.database.DatabaseAdapter;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanySearchServiceTest {

    @Mock
    TruProxyApiAdapter truProxyAPIAdapter;
    @Mock
    DatabaseAdapter databaseAdapter;
    @InjectMocks
    CompanySearchService companySearchService;

    private static final String X_API_KEY = "apiKey";
    private static final String COMPANY_STATUS_ACTIVE = "active";
    private static final String COMPANY_STATUS_INACTIVE = "inactive";

    private static final int TOTAL_NUMBER_OF_INPUT_COMPANIES = 3;
    private static final int NUMBER_OF_ACTIVE_COMPANIES = 2;

    @BeforeEach
    void setup() {
        CompaniesInputDTO companiesInputDTO = createActiveAndInactiveInputCompanies();
        OfficersInputDTO officersInputDTO = createActiveAndInactiveInputOfficers();

        when(truProxyAPIAdapter.getCompanies(X_API_KEY, "1")).thenReturn(companiesInputDTO);
        when(truProxyAPIAdapter.getOfficers(X_API_KEY, "1")).thenReturn(officersInputDTO);

        when(databaseAdapter.read(anyString(), anyString())).thenReturn(null);
        doNothing().when(databaseAdapter).save(anyString(), anyString(), any(CompaniesOutputDTO.class));
    }

    @Test
    void whenOnlyActiveFlagIsY_thenInactiveShouldNotBeReturned() {

        CompaniesOutputDTO companies = companySearchService.findByCompanyNumber(X_API_KEY, "1", "Y");

        assertThat(companies.getCompanyList().size(), equalTo(NUMBER_OF_ACTIVE_COMPANIES));
        assertThat(companies.getTotalResults(), equalTo(NUMBER_OF_ACTIVE_COMPANIES));
        assertThat(companies.getCompanyList().get(0).getCompanyStatus(), equalTo(COMPANY_STATUS_ACTIVE));
        assertThat(companies.getCompanyList().get(1).getCompanyStatus(), equalTo(COMPANY_STATUS_ACTIVE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"N", "", "anything else"})
    void whenOnlyActiveFlagIsNotY_thenActiveAndInactiveShouldBeReturned(String onlyActive) {

        CompaniesOutputDTO companies = companySearchService.findByCompanyNumber(X_API_KEY, "1", onlyActive);

        assertThat(companies.getCompanyList().size(), equalTo(TOTAL_NUMBER_OF_INPUT_COMPANIES));
        assertThat(companies.getTotalResults(), equalTo(TOTAL_NUMBER_OF_INPUT_COMPANIES));
    }

    private CompaniesInputDTO createActiveAndInactiveInputCompanies() {
        CompaniesInputDTO.Company activeCompany1 = createInputCompany(COMPANY_STATUS_ACTIVE, "1");
        CompaniesInputDTO.Company activeCompany2 = createInputCompany(COMPANY_STATUS_ACTIVE, "2");
        CompaniesInputDTO.Company inactiveCompany = createInputCompany(COMPANY_STATUS_INACTIVE, "3");
        return CompaniesInputDTO.builder().companyList(Arrays.asList(activeCompany1, activeCompany2, inactiveCompany)).build();
    }

    private CompaniesInputDTO.Company createInputCompany(String companyStatus, String uniqueId) {

        return CompaniesInputDTO.Company.builder()
                .companyNumber("1")
                .companyType("companyType" + uniqueId)
                .title("title" + uniqueId)
                .companyStatus(companyStatus)
                .dateOfCreation("2008-01-18" + uniqueId)
                .address(
                        AddressInputDTO.builder()
                                .locality("locality_active" + uniqueId)
                                .postalCode("postalCode" + uniqueId)
                                .premises("premises" + uniqueId)
                                .addressLine1("addressLine1" + uniqueId)
                                .country("country" + uniqueId).build())
                .build();
    }

    private OfficersInputDTO createActiveAndInactiveInputOfficers() {
        OfficersInputDTO.Officer activeOfficer1 = createInputOfficer(null, "1");
        OfficersInputDTO.Officer activeOfficer2 = createInputOfficer(null, "2");
        OfficersInputDTO.Officer inactiveOfficer = createInputOfficer("2012-01-07", "3");
        return OfficersInputDTO.builder().officerList(Arrays.asList(activeOfficer1, activeOfficer2, inactiveOfficer)).build();
    }

    private OfficersInputDTO.Officer createInputOfficer(String resignedOn, String uniqueId) {

        return OfficersInputDTO.Officer.builder()
                .name("name" + uniqueId)
                .appointedOn("2010-02-03" + uniqueId)
                .officerRole("officerRole" + uniqueId)
                .resignedOn(resignedOn)
                .address(
                        AddressInputDTO.builder()
                                .locality("locality" + uniqueId)
                                .postalCode("postalCode" + uniqueId)
                                .premises("premises" + uniqueId)
                                .addressLine1("addressLine1" + uniqueId)
                                .country("country" + uniqueId).build())
                .build();
    }
}
