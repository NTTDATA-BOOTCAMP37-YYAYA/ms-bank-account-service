package com.nttdata.bootcamp.msbankaccount.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nttdata.bootcamp.msbankaccount.application.outgoing.CreateAccountPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindAccounstByCustIdAndProdIdPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindAccountByIdPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindAccountByNumberPort;
import com.nttdata.bootcamp.msbankaccount.domain.enums.States;
import com.nttdata.bootcamp.msbankaccount.domain.model.Account;
import com.nttdata.bootcamp.msbankaccount.domain.util.Constants;
import com.nttdata.bootcamp.msbankaccount.infrastructure.persistence.entity.AccountEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**.*/
@Component
public class AccountRepositoryAdapter implements CreateAccountPort,
                                                 FindAccounstByCustIdAndProdIdPort,
                                                 FindAccountByNumberPort,
                                                 FindAccountByIdPort{

  @Autowired
  private ReactiveMongoAccountRepository reactiveMongoDB;


  @Override
  public Mono<Account> createAccount(Account account) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	LocalDateTime now = LocalDateTime.now();
    String createDate = now.format(formatter);
    account.setAccountCreateDate(createDate);
    account.setAccountState(States.ACTIVE.getValue());
    account.setAccountCci(Constants.CCI.concat(account.getAccountNumber()));
    return reactiveMongoDB.insert(AccountEntity.toAccountEntity(account))
                                 .map(AccountEntity::toAccount);
  }

  @Override
  public Flux<Account> findAccountsByCustIdAndProdId(String customerId, String productId) {
    return reactiveMongoDB.findAccountByCustIdAndProdId(customerId, productId,States.ACTIVE.getValue())
                          .map(AccountEntity::toAccount);
  }

  @Override
  public Mono<Account> findAccountByNumber(String accountNumber) {
    return reactiveMongoDB.findAccountByNumber(accountNumber, States.ACTIVE.getValue())
                          .map(AccountEntity::toAccount);
  }
  
  @Override
  public Mono<Account> findAccountById(String accountId) {
    return reactiveMongoDB.findById(accountId)
                          .map(AccountEntity::toAccount);
  }


}
