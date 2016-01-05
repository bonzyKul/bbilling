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
 * A AccountFamily.
 */
@Entity
@Table(name = "account_family")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accountfamily")
public class AccountFamily implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "account_family_code", length = 20, nullable = false)
    private String accountFamilyCode;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "account_family_desc", length = 255, nullable = false)
    private String accountFamilyDesc;

    @OneToMany(mappedBy = "accountFamily")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Accounts> accountss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountFamilyCode() {
        return accountFamilyCode;
    }

    public void setAccountFamilyCode(String accountFamilyCode) {
        this.accountFamilyCode = accountFamilyCode;
    }

    public String getAccountFamilyDesc() {
        return accountFamilyDesc;
    }

    public void setAccountFamilyDesc(String accountFamilyDesc) {
        this.accountFamilyDesc = accountFamilyDesc;
    }

    public Set<Accounts> getAccountss() {
        return accountss;
    }

    public void setAccountss(Set<Accounts> accountss) {
        this.accountss = accountss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountFamily accountFamily = (AccountFamily) o;

        if ( ! Objects.equals(id, accountFamily.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountFamily{" +
            "id=" + id +
            ", accountFamilyCode='" + accountFamilyCode + "'" +
            ", accountFamilyDesc='" + accountFamilyDesc + "'" +
            '}';
    }
}
