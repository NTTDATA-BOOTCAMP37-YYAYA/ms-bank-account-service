package com.nttdata.bootcamp.msbankaccount.application.incoming;

import com.nttdata.bootcamp.msbankaccount.domain.model.Account;

import reactor.core.publisher.Mono;

/**.*/
public interface FindAccountByDebitCardUseCase {

  Mono<Account> findAccountByDebitCard(String debitCardNumber);
}
