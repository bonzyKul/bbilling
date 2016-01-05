package com.barclays.bbilling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AccountAppHLD.
 */
@Entity
@Table(name = "account_app_hld")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accountapphld")
public class AccountAppHLD implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "app_hldstart_date", nullable = false)
    private LocalDate appHLDStartDate;

    @NotNull
    @Column(name = "app_hldend_date", nullable = false)
    private LocalDate appHLDEndDate;

    @ManyToOne
    private Accounts accounts;

    @ManyToOne
    private Products products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppHLDStartDate() {
        return appHLDStartDate;
    }

    public void setAppHLDStartDate(LocalDate appHLDStartDate) {
        this.appHLDStartDate = appHLDStartDate;
    }

    public LocalDate getAppHLDEndDate() {
        return appHLDEndDate;
    }

    public void setAppHLDEndDate(LocalDate appHLDEndDate) {
        this.appHLDEndDate = appHLDEndDate;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountAppHLD accountAppHLD = (AccountAppHLD) o;

        if ( ! Objects.equals(id, accountAppHLD.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountAppHLD{" +
            "id=" + id +
            ", appHLDStartDate='" + appHLDStartDate + "'" +
            ", appHLDEndDate='" + appHLDEndDate + "'" +
            '}';
    }
}
