package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.AccountAppHLD;

import com.barclays.bbilling.domain.Accounts;
import com.barclays.bbilling.domain.Products;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountAppHLD entity.
 */
public interface AccountAppHLDRepository extends JpaRepository<AccountAppHLD,Long> {

    AccountAppHLD findOneByAccountsAndProducts(Accounts accounts, Products products);

}
