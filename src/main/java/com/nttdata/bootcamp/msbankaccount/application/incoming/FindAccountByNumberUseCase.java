package com.nttdata.bootcamp.msbankaccount.application.incoming;

import com.nttdata.bootcamp.msbankaccount.domain.model.Account;

import reactor.core.publisher.Mono;

/**.*/
public interface FindAccountByNumberUseCase {

  Mono<Account> findAccountByNumber(String accountNumber);
}
