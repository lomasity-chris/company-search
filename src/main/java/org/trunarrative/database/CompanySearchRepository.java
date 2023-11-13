package org.trunarrative.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CompanySearchRepository extends CrudRepository<CompaniesEntity, CompaniesEntity.CompaniesId> {
}
