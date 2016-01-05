package com.barclays.bbilling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.barclays.bbilling.domain.enumeration.CustomerAddressType;

/**
 * A CustomerAddress.
 */
@Entity
@Table(name = "customer_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customeraddress")
public class CustomerAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_add_type", nullable = false)
    private CustomerAddressType customerAddType;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "customer_add_line_one", length = 255, nullable = false)
    private String customerAddLineOne;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "customer_add_line_two", length = 255, nullable = false)
    private String customerAddLineTwo;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "customer_city", length = 100, nullable = false)
    private String customerCity;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "customer_state", length = 100, nullable = false)
    private String customerState;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "customer_add_zip", length = 20, nullable = false)
    private String customerAddZip;

    @ManyToOne
    private Country country;

    @ManyToOne
    private Customer customer;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerAddress customerAddress = (CustomerAddress) o;

        if ( ! Objects.equals(id, customerAddress.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
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
