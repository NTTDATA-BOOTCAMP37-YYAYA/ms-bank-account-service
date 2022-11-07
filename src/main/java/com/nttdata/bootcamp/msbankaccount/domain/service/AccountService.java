package com.nttdata.bootcamp.msbankaccount.domain.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.msbankaccount.application.incoming.CreateAccountUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.FindAccountByDebitCardUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.FindAccountByIdUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.FindAccountByNumberUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.ValidateOpeningAccountUseCase;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.CreateAccountPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindAccounstByCustIdAndProdIdPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindAccountByIdPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindAccountByNumberPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindBusinessRulesOfProductPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindCreditCardsCustomerPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindCreditDebtsCustomerPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindDebitCardByNumberPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.SendAccountBalancePort;
import com.nttdata.bootcamp.msbankaccount.domain.enums.OpeningRuleIds;
import com.nttdata.bootcamp.msbankaccount.domain.model.Account;
import com.nttdata.bootcamp.msbankaccount.domain.model.AccountBalance;
import com.nttdata.bootcamp.msbankaccount.domain.model.Credit;
import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;
import com.nttdata.bootcamp.msbankaccount.domain.model.ValidateOpeningAccount;
import com.nttdata.bootcamp.msbankaccount.domain.util.Constants;

import reactor.core.publisher.Mono;

/**.*/
@Service
public class AccountService implements CreateAccountUseCase,
                                       ValidateOpeningAccountUseCase,
                                       FindAccountByNumberUseCase,
                                       FindAccountByDebitCardUseCase,
                                       FindAccountByIdUseCase{

  final  Logger logger = LoggerFactory.getLogger(AccountService.class);

	
  @Autowired
  private CreateAccountPort createAccountPort;
  @Autowired
  private FindAccounstByCustIdAndProdIdPort findAccountsByCustIdAndProdIdPort;
  @Autowired
  private FindBusinessRulesOfProductPort findBusinessRulesOfProductPort;
  @Autowired
  private FindCreditDebtsCustomerPort findCreditDebtsCustomerPort;
  @Autowired
  private FindCreditCardsCustomerPort findCreditCardsCustomerPort;
  @Autowired
  private FindAccountByNumberPort findAccountByNumberPort;
  @Autowired
  private FindDebitCardByNumberPort findDebitCardByNumberPort;
  @Autowired
  private SendAccountBalancePort sendAccountBalancePort;
  @Autowired
  private FindAccountByIdPort findAccountByIdPort;

  @Override
  public Mono<Account> createAccount(Account account) {
    
    return findBusinessRulesOfProductPort.findBusinessRulesOfProduct(
        account.getProductId(),
        account.getCustomerTypeId(),
        account.getCustomerCategoryId(),
        Constants.OPENING_PROCESS)
        .flatMap(rules  ->!rules.isEmpty()
                ? this.validateOpeningAccount(account.getCustomerId(),  account.getProductId(), rules)
                : Mono.empty())
        .flatMap(validate ->createAccountPort.createAccount(account))
        .flatMap(newAccount -> {
        	 AccountBalance accountBalance = new AccountBalance();
        	 accountBalance.setAccountId(newAccount.getAccountId());
        	 accountBalance.setAccountBalanceAmount(0);
        	 accountBalance.setAccountBalanceQuantityTransaction(0);
        	 this.sendAccountBalance(accountBalance);
        	 return Mono.just(newAccount);
        });
  }
  
  
  @Override
  public Mono<ValidateOpeningAccount> validateOpeningAccount(String customerId, 
                                                             String productId,
                                                             List<Rule> rules){
    
	Mono<ValidateOpeningAccount> validate=Mono.empty();
    Mono<Optional<List<Account>>> fluxAccounts = Mono.just(Optional.empty());
    Mono<Optional<List<Credit>>> fluxCreditDebts = Mono.just(Optional.empty());
    Mono<Optional<List<Credit>>> fluxCreditCards = Mono.just(Optional.empty());
    
    
    
    if (FindBusinessRulesOfProductPort.compare(rules, OpeningRuleIds.MAXQUANTPRODUCT.getValue())) {
      
      fluxAccounts = findAccountsByCustIdAndProdIdPort
                    .findAccountsByCustIdAndProdId(customerId, productId)
                    .collectList().map(Optional::of);
    }

    if (FindBusinessRulesOfProductPort.compare(rules, OpeningRuleIds.MAXCREDDEBT.getValue())) {
      
      fluxCreditDebts = findCreditDebtsCustomerPort
                      .findCreditDebtsCustomer(customerId)
                      .collectList().map(Optional::of);
    }
    if (FindBusinessRulesOfProductPort.compare(rules, OpeningRuleIds.MINCREDCARD.getValue())) {
      
      fluxCreditCards = findCreditCardsCustomerPort
                     .findCreditCardsCustomer(customerId)
                     .collectList().map(Optional::of);
    }


    validate= Mono.zip(fluxAccounts,
                                                                 fluxCreditDebts,
                                                                 fluxCreditCards)
        .map(tupla -> {
          
          Boolean validateMaxQuantityProduct = Boolean.TRUE;
          Boolean validateCreditDebt = Boolean.TRUE;
          Boolean validateCreditCard = Boolean.TRUE;
          
          StrBuilder message = new StrBuilder();
          
          
          
          Optional<Rule> ruleMaxProducts = rules.stream()
              .filter(r -> r.getRuleId().equals(OpeningRuleIds.MAXQUANTPRODUCT.getValue())).findFirst();
          Optional<Rule> ruleMaxCreddebt = rules.stream()
              .filter(r -> r.getRuleId().equals(OpeningRuleIds.MAXCREDDEBT.getValue())).findFirst();
          Optional<Rule> ruleMinCredCard = rules.stream()
              .filter(r -> r.getRuleId().equals(OpeningRuleIds.MINCREDCARD.getValue())).findFirst();
          
          if (ruleMaxProducts.isPresent() && tupla.getT1().isPresent() && !tupla.getT1().isEmpty()) {
            validateMaxQuantityProduct = FindAccounstByCustIdAndProdIdPort
                                       .validateMaxQuantityProduct(ruleMaxProducts.get(),
                                       tupla.getT1().get().stream().count());
            message.append(validateMaxQuantityProduct.equals(Boolean.FALSE) 
                ? " Account cannot be opened - Exceeding the maximum quantity allowed "
                : "");
          }
          if (ruleMaxCreddebt.isPresent() && tupla.getT2().isPresent() && !tupla.getT2().isEmpty()) {
            validateCreditDebt = FindCreditDebtsCustomerPort
                                      .validateCreditDebt(ruleMaxCreddebt.get(),
                                       tupla.getT2().get().stream().count());
            message.append(validateCreditDebt.equals(Boolean.FALSE)
                ? " Account cannot be opened - Customer have a debt "
                : "");
          }
          if (ruleMinCredCard.isPresent()) {
            if (tupla.getT3().isPresent() && !tupla.getT1().isEmpty()) {
              validateCreditCard = FindCreditCardsCustomerPort
                                      .validateCreditCard(ruleMinCredCard.get(),
                                      tupla.getT3().get().stream().count()); 
            } else {
              validateCreditCard = Boolean.FALSE;
            }

          }
          message.append(validateCreditCard.equals(Boolean.FALSE)
              ? " Account cannot be opened - Customer don't have credit card "
              : "");
          ValidateOpeningAccount validateOpening = 
                                  new ValidateOpeningAccount(validateMaxQuantityProduct 
                                  && validateCreditDebt 
                                  && validateCreditCard,
                                  message.toString());
          return validateOpening;
        });
    
    
    return validate.flatMap(
    		v ->{
	    			if(v.getValidate()) {
	    				return Mono.just(v);
	    			}else {
	    				return Mono.error(new Exception(v.getMessage()));
	    			}
    			}
    		);
  }


  @Override
  public Mono<Account> findAccountByNumber(String accountNumber) {
    return findAccountByNumberPort.findAccountByNumber(accountNumber);
  }


  
  @Override
  public Mono<Account> findAccountByDebitCard(String debitCardNumber) {
	return findDebitCardByNumberPort.findDebitCardByNumber(debitCardNumber)
				.flatMap(d ->findAccountByIdPort.findAccountById(d.getAccountIdMainAssociated()))
				.switchIfEmpty(Mono.empty());
  }


	@Override
	public Mono<Account> findAccountById(String accountId) {
		return findAccountByIdPort.findAccountById(accountId);
	}
  

  /**.*/
  public AccountBalance sendAccountBalance(AccountBalance accountBalance) {
    if (accountBalance != null) {
     logger.info("Send  AccountBalance {} ", accountBalance);
      sendAccountBalancePort.sendAccountBalance(accountBalance);
    }
    return accountBalance;
  }

 

}
