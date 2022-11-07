package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.domain.model.Account;

import reactor.core.publisher.Mono;

/**.*/
public interface FindAccountByIdPort {

  Mono<Account> findAccountById(String accountId);
}
