package com.barclays.bbilling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A ServiceCatalogue.
 */
@Entity
@Table(name = "service_catalogue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servicecatalogue")
public class ServiceCatalogue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "service_code", nullable = false)
    private String serviceCode;

    @NotNull
    @Column(name = "service_description", nullable = false)
    private String serviceDescription;

    @NotNull
    @Column(name = "service_start_date", nullable = false)
    private LocalDate serviceStartDate;

    @NotNull
    @Column(name = "service_end_date", nullable = false)
    private LocalDate serviceEndDate;

    @ManyToOne
    private Products products;

    @OneToMany(mappedBy = "serviceCatalogue")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pricing> pricings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public LocalDate getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDate serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDate getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDate serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Set<Pricing> getPricings() {
        return pricings;
    }

    public void setPricings(Set<Pricing> pricings) {
        this.pricings = pricings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceCatalogue serviceCatalogue = (ServiceCatalogue) o;

        if ( ! Objects.equals(id, serviceCatalogue.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceCatalogue{" +
            "id=" + id +
            ", serviceCode='" + serviceCode + "'" +
            ", serviceDescription='" + serviceDescription + "'" +
            ", serviceStartDate='" + serviceStartDate + "'" +
            ", serviceEndDate='" + serviceEndDate + "'" +
            '}';
    }
}
