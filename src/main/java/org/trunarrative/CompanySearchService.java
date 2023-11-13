package org.trunarrative;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trunarrative.domain.*;
import org.trunarrative.database.DatabaseAdapter;

import java.util.List;

@Service
@Slf4j
public class CompanySearchService {

    private static final String COMPANY_STATUS_ACTIVE = "active";
    private static final String ONLY_ACTIVE_YES = "Y";

    @Autowired
    private TruProxyApiAdapter truProxyAPIAdapter;

    @Autowired
    private DatabaseAdapter databaseAdapter;

    CompaniesOutputDTO findByCompanyName(@NonNull String xApiKey, @NonNull String companyName, String onlyActive) {
        CompaniesInputDTO inputCompanies = truProxyAPIAdapter.getCompanies(xApiKey, companyName);
        return createCompaniesOutputDTO(xApiKey, inputCompanies, onlyActive);
    }

    CompaniesOutputDTO findByCompanyNumber(@NonNull String xApiKey, @NonNull String companyNumber, String onlyActive) {

        CompaniesOutputDTO companiesOutputDTO = databaseAdapter.read(companyNumber, onlyActive);

        if (companiesOutputDTO == null) {
            CompaniesInputDTO inputCompanies = truProxyAPIAdapter.getCompanies(xApiKey, companyNumber);
            companiesOutputDTO = createCompaniesOutputDTO(xApiKey, inputCompanies, onlyActive);

            databaseAdapter.save(companyNumber, onlyActive, companiesOutputDTO);
        }
        return companiesOutputDTO;
    }

    private CompaniesOutputDTO createCompaniesOutputDTO(String xApiKey, CompaniesInputDTO inputCompanies, String onlyActive) {

        List<CompaniesOutputDTO.Company> companyList
                = inputCompanies.getCompanyList().stream()
                .filter(company -> COMPANY_STATUS_ACTIVE.equals(company.getCompanyStatus()) || !ONLY_ACTIVE_YES.equalsIgnoreCase(onlyActive))
                .map(this::mapCompany)
                .toList();

        companyList.forEach(company -> {

            OfficersInputDTO inputOfficers = truProxyAPIAdapter.getOfficers(xApiKey, company.getCompanyNumber());

            List<CompaniesOutputDTO.Officer> outputOfficerList = null;
            if (inputOfficers.getOfficerList() != null) {
                outputOfficerList
                        = inputOfficers.getOfficerList().stream()
                        .filter(inputOfficer -> inputOfficer.getResignedOn() == null)
                        .map(this::mapOfficer)
                        .toList();
            }
            company.setOfficers(outputOfficerList);
        });

        return CompaniesOutputDTO.builder().totalResults(companyList.size()).companyList(companyList).build();
    }

    private CompaniesOutputDTO.Company mapCompany(CompaniesInputDTO.Company inputCompany) {
        return CompaniesOutputDTO.Company.builder()
                .companyNumber(inputCompany.getCompanyNumber())
                .companyType(inputCompany.getCompanyType())
                .title((inputCompany.getTitle()))
                .companyStatus(inputCompany.getCompanyStatus())
                .dateOfCreation(inputCompany.getDateOfCreation())
                .address(mapAddress(inputCompany.getAddress()))
                .build();
    }

    private CompaniesOutputDTO.Address mapAddress(AddressInputDTO inputAddress) {
        CompaniesOutputDTO.Address mappedAddress = null;
        if (inputAddress != null) {
            mappedAddress = CompaniesOutputDTO.Address.builder()
                    .locality(inputAddress.getLocality())
                    .postalCode(inputAddress.getPostalCode())
                    .premises(inputAddress.getPremises())
                    .addressLine1(inputAddress.getAddressLine1())
                    .country(inputAddress.getCountry())
                    .build();
        }
        return mappedAddress;
    }

    private CompaniesOutputDTO.Officer mapOfficer(OfficersInputDTO.Officer inputOfficer) {
        return CompaniesOutputDTO.Officer.builder()
                .name(inputOfficer.getName())
                .appointedOn(inputOfficer.getAppointedOn())
                .officerRole(inputOfficer.getOfficerRole())
                .address(mapAddress(inputOfficer.getAddress()))
                .build();
    }
}
