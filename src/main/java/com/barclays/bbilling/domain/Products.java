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

import com.barclays.bbilling.domain.enumeration.ProductStatus;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "products")
public class Products implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "product_code", length = 20, nullable = false)
    private String productCode;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "product_short_name", length = 20, nullable = false)
    private String productShortName;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_start_date", nullable = false)
    private LocalDate productStartDate;

    @NotNull
    @Column(name = "product_end_date", nullable = false)
    private LocalDate productEndDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus productStatus;

    @ManyToOne
    private ProductFamily productFamily;

    @OneToMany(mappedBy = "products")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceCatalogue> serviceCatalogues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductShortName() {
        return productShortName;
    }

    public void setProductShortName(String productShortName) {
        this.productShortName = productShortName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getProductStartDate() {
        return productStartDate;
    }

    public void setProductStartDate(LocalDate productStartDate) {
        this.productStartDate = productStartDate;
    }

    public LocalDate getProductEndDate() {
        return productEndDate;
    }

    public void setProductEndDate(LocalDate productEndDate) {
        this.productEndDate = productEndDate;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public ProductFamily getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(ProductFamily productFamily) {
        this.productFamily = productFamily;
    }

    public Set<ServiceCatalogue> getServiceCatalogues() {
        return serviceCatalogues;
    }

    public void setServiceCatalogues(Set<ServiceCatalogue> serviceCatalogues) {
        this.serviceCatalogues = serviceCatalogues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Products products = (Products) o;

        if ( ! Objects.equals(id, products.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Products{" +
            "id=" + id +
            ", productCode='" + productCode + "'" +
            ", productShortName='" + productShortName + "'" +
            ", productName='" + productName + "'" +
            ", productStartDate='" + productStartDate + "'" +
            ", productEndDate='" + productEndDate + "'" +
            ", productStatus='" + productStatus + "'" +
            '}';
    }
}
