package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.barclays.bbilling.domain.enumeration.CustomerAddressType;

/**
 * A DTO for the CustomerAddress entity.
 */
public class CustomerAddressDTO implements Serializable {

    private Long id;

    @NotNull
    private CustomerAddressType customerAddType;

    @NotNull
    @Size(min = 1, max = 255)
    private String customerAddLineOne;

    @NotNull
    @Size(min = 1, max = 255)
    private String customerAddLineTwo;

    @NotNull
    @Size(min = 1, max = 100)
    private String customerCity;

    @NotNull
    @Size(min = 1, max = 100)
    private String customerState;

    @NotNull
    @Size(min = 1, max = 20)
    private String customerAddZip;

    private Long countryId;

    private String countryCountryName;

    private Long customerId;

    private String customerCustomerID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerAddressType getCustomerAddType() {
        return customerAddType;
    }

    public void setCustomerAddType(CustomerAddressType customerAddType) {
        this.customerAddType = customerAddType;
    }

    public String getCustomerAddLineOne() {
        return customerAddLineOne;
    }

    public void setCustomerAddLineOne(String customerAddLineOne) {
        this.customerAddLineOne = customerAddLineOne;
    }

    public String getCustomerAddLineTwo() {
        return customerAddLineTwo;
    }

    public void setCustomerAddLineTwo(String customerAddLineTwo) {
        this.customerAddLineTwo = customerAddLineTwo;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomerAddZip() {
        return customerAddZip;
    }

    public void setCustomerAddZip(String customerAddZip) {
        this.customerAddZip = customerAddZip;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCustomerID() {
        return customerCustomerID;
    }

    public void setCustomerCustomerID(String customerCustomerID) {
        this.customerCustomerID = customerCustomerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerAddressDTO customerAddressDTO = (CustomerAddressDTO) o;

        if ( ! Objects.equals(id, customerAddressDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerAddressDTO{" +
            "id=" + id +
            ", customerAddType='" + customerAddType + "'" +
            ", customerAddLineOne='" + customerAddLineOne + "'" +
            ", customerAddLineTwo='" + customerAddLineTwo + "'" +
            ", customerCity='" + customerCity + "'" +
            ", customerState='" + customerState + "'" +
            ", customerAddZip='" + customerAddZip + "'" +
            '}';
    }
}
