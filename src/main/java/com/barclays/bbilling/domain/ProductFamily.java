package com.barclays.bbilling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProductFamily.
 */
@Entity
@Table(name = "product_family")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productfamily")
public class ProductFamily implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "product_family_code", length = 20, nullable = false)
    private String productFamilyCode;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "product_family_desc", length = 255, nullable = false)
    private String productFamilyDesc;

    @OneToMany(mappedBy = "productFamily")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Products> productss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductFamilyCode() {
        return productFamilyCode;
    }

    public void setProductFamilyCode(String productFamilyCode) {
        this.productFamilyCode = productFamilyCode;
    }

    public String getProductFamilyDesc() {
        return productFamilyDesc;
    }

    public void setProductFamilyDesc(String productFamilyDesc) {
        this.productFamilyDesc = productFamilyDesc;
    }

    public Set<Products> getProductss() {
        return productss;
    }

    public void setProductss(Set<Products> productss) {
        this.productss = productss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductFamily productFamily = (ProductFamily) o;

        if ( ! Objects.equals(id, productFamily.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductFamily{" +
            "id=" + id +
            ", productFamilyCode='" + productFamilyCode + "'" +
            ", productFamilyDesc='" + productFamilyDesc + "'" +
            '}';
    }
}
