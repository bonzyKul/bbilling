package com.barclays.bbilling.service;

import com.barclays.bbilling.Facts.PricingFact;
import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.domain.Currency;
import com.barclays.bbilling.domain.enumeration.*;
import com.barclays.bbilling.repository.*;
import com.barclays.bbilling.web.rest.dto.PricingDTO;
import com.barclays.bbilling.web.rest.mapper.PricingMapper;
import com.google.common.collect.Lists;
import org.drools.core.ClassObjectFilter;
import org.drools.core.common.DefaultFactHandle;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.*;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;


@Service
@Lazy(false)
@Transactional
public class ChargingService {

    private final Logger log = LoggerFactory.getLogger(ChargingService.class);
    private Boolean executeDate = false;


    private final KieContainer kieContainer;

    @Inject
    private AccountsRepository accountsRepository;

    @Inject
    private ProductsRepository productsRepository;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private PricingRepository pricingRepository;

    @Inject
    private AccountAppHLDRepository accountAppHLDRepository;

    @Inject
    private PricingMapper pricingMapper;

    @Inject
    private ProductFamilyRepository productFamilyRepository;

    @Inject
    private AccountFamilyRepository accountFamilyRepository;

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CurrencyRepository currencyRepository;

    @Inject
    private BankRepository bankRepository;

    @Inject
    private BranchRepository branchRepository;


    @Autowired
    public ChargingService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }



    /**
     *service to validate the incoming account, customer, event and charge accordingly.
     */
    public String getCharge(String eventMaster) {

        PricingDTO pricingDTO = new PricingDTO();
        PricingFact pricingFact = new PricingFact();
        String accounts = "121";
        String productCode = "MP001";

        if (executeDate == false) {
            insertData();
            executeDate = true;
        }

        if (accounts != null) {
            Accounts acc = accountsRepository.findOneByAccountNumber(accounts);

            if (acc.getAccountStatus() == AccountStatus.ACTIVE) {

                Products products = productsRepository.findOneByProductCode(productCode);
                if (products.getId() != null) {
                    AccountAppHLD accountAppHLD = accountAppHLDRepository.findOneByAccountsAndProducts(acc, products);
                    ZonedDateTime now = ZonedDateTime.now();

                    if ((now.toLocalDate().isAfter(accountAppHLD.getAppHLDStartDate()) && now.toLocalDate().isBefore(accountAppHLD.getAppHLDEndDate()))) {
                        System.out.println("Account and Product are Valid and can determine Pricing " + products.getId());
                        Pricing pricing = pricingRepository.findOneByProductsAndPricingForStaff(products, false);

                        pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

                        List<PricingTier> pricingTierList = Lists.newArrayList(pricing.getPricingTiers());

                        System.out.println("Size of pricing tier list : - " + pricingTierList.size());

                        System.out.println("=================================================================================================");
                        System.out.println("Pricing retrieved " + pricingDTO.getPricingType() + " " + pricingDTO.getPricingChargeAmount());
                        System.out.println("=================================================================================================");
                    } else {
                        System.out.println("Account and Product linkage is Expired");
                    }
                } else {
                    System.out.println("Product not available !!");
                }
            } else {
                System.out.println("Account not Active. Record has been added to Exception table");
            }
        }

        Boolean success = processRules(pricingDTO);
        if (success) {
            System.out.println("=================================================================================================");
            System.out.println("Rules fired ");
            System.out.println("=================================================================================================");
            return "Success in retrieving Pricing";
        }
        return "Error while processing Rules or Incorrect Data !!";
    }

    public Boolean processRules(Object o) {
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");

        FactHandle factHandle = kieSession.insert(o);
        System.out.println(((DefaultFactHandle) factHandle).getObject());
        Collection<FactHandle> factHandles = kieSession.getFactHandles(new ClassObjectFilter(PricingDTO.class));
        System.out.println("=================================================================================================");
        System.out.println("number of fact handles for Pricing FACT in working memory:- " + factHandles.size());
        System.out.println("=================================================================================================");
        System.out.println("Fact count in the session : - " + kieSession.getFactCount());
        System.out.println("=================================================================================================");



        kieSession.getAgenda().getAgendaGroup("FLAT_PRICING").setFocus();

        int ruleFiredCount = kieSession.fireAllRules();



        System.out.println("=================================================================================================");
        System.out.println("Number of Rules Fired count : - " + ruleFiredCount);
        System.out.println("=================================================================================================");
        System.out.println("=================================================================================================");
        factHandle = factHandles.iterator().next();
//        pricingFact = (PricingFact)((DefaultFactHandle) factHandle).getObject();

        kieSession.delete(factHandle);
//        ChargeMaster chargeMaster = findBilling(kieSession);
        kieSession.dispose();

        if (ruleFiredCount > 0) {
            return true;
        }
        return false;
    }


    public void insertData() {
        ZonedDateTime now = ZonedDateTime.now();
        Country country = new Country();
        country.setId(1L);
        country.setCountryCode("UK");
        country.setCountryName("United Kingdom");
        countryRepository.save(country);

        Currency currency = new Currency();
        currency.setId(1L);
        currency.setCurrencyCode("GBP");
        currency.setCurrencyDescription("Pounds");
        currency.setCountry(country);
        currencyRepository.save(currency);

        ProductFamily productFamily = new ProductFamily();
        productFamily.setId(1L);
        productFamily.setProductFamilyCode("UKBA");
        productFamily.setProductFamilyDesc("UKBA");
        productFamilyRepository.save(productFamily);

        AccountFamily accountFamily = new AccountFamily();
        accountFamily.setId(1L);
        accountFamily.setAccountFamilyCode("UKBA");
        accountFamily.setAccountFamilyDesc("UKBA");
        accountFamilyRepository.save(accountFamily);

        Bank bank = new Bank();
        bank.setId(1L);
        bank.setBankCode("B");
        bank.setBankName("Barclays");
        bankRepository.save(bank);


        Branch branch = new Branch();
        branch.setId(1L);
        branch.setBranchCode("1");
        branch.setBranchName("2 CP");
        branchRepository.save(branch);


        Products products = new Products();
        products.setId(1L);
        products.setProductCode("MP001");
        products.setProductShortName("MP001");
        products.setProductName("Monthly Fee");
        products.setProductStartDate(now.toLocalDate().minusYears(2));
        products.setProductEndDate(now.toLocalDate().plusYears(9999));
        products.setProductStatus(ProductStatus.Active);
        products.setProductFamily(productFamily);
        productsRepository.save(products);


        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCustomerID(1);
        customer.setCustomerFName("Alok");
        customer.setCustomerMName("A");
        customer.setCustomerLName("Kulkarni");
        customer.setCustomerStartDate(now.toLocalDate().minusYears(1));
        customer.setCustomerEndDate(now.toLocalDate().plusYears(9999));
        customer.setCustomerStatus(CustomerStatus.Active);
        customer.setCustomerTier(CustomerTier.TIER1);
        customer.setCustomerEmail("kulkarni.alok@gmail.com");
        customer.setCustomerTelNo("1");
        customer.setCustomerMobileNo("9673992686");
        customer.setCountry(country);
        customer.setCustomerType(CustomerType.NORMAL);
        customerRepository.save(customer);

        Accounts accounts = new Accounts();
        accounts.setId(1L);
        accounts.setAccountNumber("121");
        accounts.setAccountType(AccountType.CA);
        accounts.setAccountTier(AccountTier.TIER1);
        accounts.setAccountStatus(AccountStatus.ACTIVE);
        accounts.setAccountOpenedDate(now.toLocalDate().minusYears(1));
        accounts.setAccountClosedDate(now.toLocalDate().plusYears(9999));
        accounts.setAccountAvailBal(new BigDecimal(50));
        accounts.setAccountBalance(new BigDecimal(50));
        accounts.setAccountChargingBal(new BigDecimal(50));
        accounts.setAccountCRTurnOver(new BigDecimal(50));
        accounts.setAccountDRTurnOver(new BigDecimal(50));
        accounts.setAccountLedgerBal(new BigDecimal(50));
        accounts.setAccountLastBalType(AccountLastBalType.CR);
        accounts.setAccountFamily(accountFamily);
        accounts.setBank(bank);
        accounts.setBranch(branch);
        accounts.setProducts(products);
        accounts.setCurrency(currency);
        accounts.setCountry(country);
        accounts.setCustomer(customer);
        accountsRepository.save(accounts);

        AccountAppHLD accountAppHLD = new AccountAppHLD();
        accountAppHLD.setId(1L);
        accountAppHLD.setAppHLDStartDate(now.toLocalDate().minusYears(1));
        accountAppHLD.setAppHLDEndDate(now.toLocalDate().plusYears(9999));
        accountAppHLD.setAccounts(accounts);
        accountAppHLD.setProducts(products);
        accountAppHLDRepository.save(accountAppHLD);

        Pricing pricing = new Pricing();
        pricing.setId(1L);
        pricing.setPricingType(PricingType.FLAT_SIMPLE);
        pricing.setPricingAmountType(PricingAmountType.DR);
        pricing.setPricingChargeAmount(new BigDecimal(1));
        pricing.setPricingStartDate(now.toLocalDate().minusYears(2));
        pricing.setPricingEndDate(now.toLocalDate().plusYears(9999));
        pricing.setPricingForStaff(false);
        pricing.setPricingRateType(PricingRateType.FLAT);
        pricing.setPricingUnit(1);
        pricing.setPricingUnitType(PricingUnitType.VALUE);
        pricing.setCurrency(currency);
        pricing.setProducts(products);
        pricing.setPricingTaxIndicator(false);
        pricingRepository.save(pricing);
    }


    /**
     * Search the {@link KieSession} for charge records.
     */
//    private ChargeMaster findBilling(KieSession kieSession) {
//
//        // Find all BusPass facts and 1st generation child classes of BusPass.
//        ObjectFilter chargeMasterFilter = new ObjectFilter() {
//            @Override
//            public boolean accept(Object object) {
//                if (ChargeMaster.class.equals(object.getClass())) return true;
//                if (ChargeMaster.class.equals(object.getClass().getSuperclass())) return true;
//                return false;
//            }
//        };
//
//        // printFactsMessage(kieSession);
//
//        List<ChargeMaster> facts = new ArrayList<>();
//        for (FactHandle handle : kieSession.getFactHandles(chargeMasterFilter)) {
//            facts.add((ChargeMaster) kieSession.getObject(handle));
//        }
//        if (facts.size() == 0) {
//            return null;
//        }
//        // Assumes that the rules will always be generating a single bus pass.
//        return facts.get(0);
//    }



    /**
     * Job to search for accounts with Anniversary is Today and credit their account with respective rewards
     *
     * this job runs every 1am in the morning for all 7 days. Just change the cron job parameter to reflect as 0 0 1 * * ?
     * you can also configure the cron job parameters in a properties files and change it at runtime or as and when required
     *
     */

    @Scheduled(cron = "${jobSchedule.annivarsary.anivParameter}")
    public void rewardforAnniversary() {
        ZonedDateTime now = ZonedDateTime.now();

        //getCharge method called to charge the transactions

        String myCharge = getCharge("charge");
        System.out.println("=================================================================================================");
        System.out.println("Output from Rules " + myCharge);
        System.out.println("=================================================================================================");

        //get all the accounts that have an anivarsary today and credit them the reward

        System.out.println("in scheduled account opened job " + now.toLocalDateTime() + "  " + now.toLocalDate().minusYears(1));
        List<Accounts> accountsList= accountsRepository.findAllByAccountOpenedDate(now.toLocalDate().minusYears(1));
        if (accountsList.isEmpty()) {
            System.out.println("=================================================================================================");
            System.out.println("No accounts with Anniversary Date of today !!!");
            System.out.println("=================================================================================================");
        } else {
            for (Accounts accounts : accountsList) {
                System.out.println("=================================================================================================");
                System.out.println("=================================================================================================");
                System.out.println("This account " + accounts.getAccountNumber() + " has an anniversary today");
                System.out.println("=================================================================================================");
                System.out.println("=================================================================================================");
            }
        }
    }

}
