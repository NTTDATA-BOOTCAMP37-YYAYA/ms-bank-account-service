package com.nttdata.bootcamp.msbankaccount.application.incoming;

import java.util.List;

import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;
import com.nttdata.bootcamp.msbankaccount.domain.model.ValidateOpeningAccount;

import reactor.core.publisher.Mono;

/**.*/
public interface ValidateOpeningAccountUseCase {

  Mono<ValidateOpeningAccount> validateOpeningAccount(String customerId,
                                                    String productId,
                                                    List<Rule> rules);
}
