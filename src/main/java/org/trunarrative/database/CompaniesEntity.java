package org.trunarrative.database;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesEntity {

    @EmbeddedId
    private CompaniesId companiesId;
    @Getter
    @Column(columnDefinition = "TEXT")
    private String companiesOutput;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class CompaniesId implements Serializable {
        private String companyNumber;
        private String onlyActive;
    }
}
