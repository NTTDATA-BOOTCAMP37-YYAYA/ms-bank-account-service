package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.response.DebitCard;

import reactor.core.publisher.Mono;

public interface FindDebitCardByNumberPort {

	Mono<DebitCard> findDebitCardByNumber(String accountNumber);
}
