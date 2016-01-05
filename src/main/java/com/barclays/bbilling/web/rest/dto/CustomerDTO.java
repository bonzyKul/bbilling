package com.barclays.bbilling.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.barclays.bbilling.domain.enumeration.CustomerStatus;
import com.barclays.bbilling.domain.enumeration.CustomerType;
import com.barclays.bbilling.domain.enumeration.CustomerTier;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer customerID;

    @NotNull
    @Size(min = 1, max = 100)
    private String customerFName;

    @Size(min = 1, max = 100)
    private String customerMName;

    @NotNull
    @Size(min = 1, max = 100)
    private String customerLName;

    @NotNull
    private LocalDate customerStartDate;

    @NotNull
    private LocalDate customerEndDate;

    @NotNull
    private CustomerStatus customerStatus;

    @NotNull
    private CustomerType customerType;

    @NotNull
    private String customerEmail;

    @NotNull
    private String customerTelNo;

    @NotNull
    private String customerMobileNo;

    @NotNull
    private CustomerTier customerTier;

    private Long countryId;

    private String countryCountryName;

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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryCountryName() {
        return countryCountryName;
    }

    public void setCountryCountryName(String countryCountryName) {
        this.countryCountryName = countryCountryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;

        if ( ! Objects.equals(id, customerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
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
