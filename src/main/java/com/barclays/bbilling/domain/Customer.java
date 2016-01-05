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

import com.barclays.bbilling.domain.enumeration.CustomerStatus;

import com.barclays.bbilling.domain.enumeration.CustomerType;

import com.barclays.bbilling.domain.enumeration.CustomerTier;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Integer customerID;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "customer_fname", length = 100, nullable = false)
    private String customerFName;

    @Size(min = 1, max = 100)
    @Column(name = "customer_mname", length = 100)
    private String customerMName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "customer_lname", length = 100, nullable = false)
    private String customerLName;

    @NotNull
    @Column(name = "customer_start_date", nullable = false)
    private LocalDate customerStartDate;

    @NotNull
    @Column(name = "customer_end_date", nullable = false)
    private LocalDate customerEndDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status", nullable = false)
    private CustomerStatus customerStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    @NotNull
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @NotNull
    @Column(name = "customer_tel_no", nullable = false)
    private String customerTelNo;

    @NotNull
    @Column(name = "customer_mobile_no", nullable = false)
    private String customerMobileNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_tier", nullable = false)
    private CustomerTier customerTier;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CustomerAddress> customerAddresss = new HashSet<>();

    @ManyToOne
    private Country country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerFName() {
        return customerFName;
    }

    public void setCustomerFName(String customerFName) {
        this.customerFName = customerFName;
    }

    public String getCustomerMName() {
        return customerMName;
    }

    public void setCustomerMName(String customerMName) {
        this.customerMName = customerMName;
    }

    public String getCustomerLName() {
        return customerLName;
    }

    public void setCustomerLName(String customerLName) {
        this.customerLName = customerLName;
    }

    public LocalDate getCustomerStartDate() {
        return customerStartDate;
    }

    public void setCustomerStartDate(LocalDate customerStartDate) {
        this.customerStartDate = customerStartDate;
    }

    public LocalDate getCustomerEndDate() {
        return customerEndDate;
    }

    public void setCustomerEndDate(LocalDate customerEndDate) {
        this.customerEndDate = customerEndDate;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerTelNo() {
        return customerTelNo;
    }

    public void setCustomerTelNo(String customerTelNo) {
        this.customerTelNo = customerTelNo;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public CustomerTier getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(CustomerTier customerTier) {
        this.customerTier = customerTier;
    }

    public Set<CustomerAddress> getCustomerAddresss() {
        return customerAddresss;
    }

    public void setCustomerAddresss(Set<CustomerAddress> customerAddresss) {
        this.customerAddresss = customerAddresss;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Customer customer = (Customer) o;

        if ( ! Objects.equals(id, customer.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", customerID='" + customerID + "'" +
            ", customerFName='" + customerFName + "'" +
            ", customerMName='" + customerMName + "'" +
            ", customerLName='" + customerLName + "'" +
            ", customerStartDate='" + customerStartDate + "'" +
            ", customerEndDate='" + customerEndDate + "'" +
            ", customerStatus='" + customerStatus + "'" +
            ", customerType='" + customerType + "'" +
            ", customerEmail='" + customerEmail + "'" +
            ", customerTelNo='" + customerTelNo + "'" +
            ", customerMobileNo='" + customerMobileNo + "'" +
            ", customerTier='" + customerTier + "'" +
            '}';
    }
}
