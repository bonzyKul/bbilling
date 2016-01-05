package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Country entity.
 */
public class CountryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 6)
    private String countryCode;

    @NotNull
    @Size(min = 1, max = 100)
    private String countryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;

        if ( ! Objects.equals(id, countryDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + id +
            ", countryCode='" + countryCode + "'" +
            ", countryName='" + countryName + "'" +
            '}';
    }
}
