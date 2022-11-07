package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.domain.model.Credit;
import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;

import reactor.core.publisher.Flux;

/**.*/
public interface FindCreditCardsCustomerPort {

  public  Flux<Credit> findCreditCardsCustomer(String customerId);
  
  /**.*/
  static Boolean validateCreditCard(Rule rule, long quantityCreditCard) {
    
    return rule.getRuleValue().matches("[0-9]+")
        && Long.valueOf(quantityCreditCard)
        .compareTo(Long.parseLong(rule.getRuleValue())) >= 0;
  }

}
