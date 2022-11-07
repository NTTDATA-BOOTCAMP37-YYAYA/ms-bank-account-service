package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.domain.enums.Units;
import com.nttdata.bootcamp.msbankaccount.domain.model.Credit;
import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;

import reactor.core.publisher.Flux;

/**.*/
public interface FindCreditDebtsCustomerPort {

  public Flux<Credit> findCreditDebtsCustomer(String customerId);

  /**.*/
  static Boolean validateCreditDebt(Rule rule, long quantityDebts) {
    

    return (rule.getRuleValue().matches("[0-9]+") 
        && Long.valueOf(quantityDebts).compareTo(Long.parseLong(rule.getRuleValue()))<= 0)
        || rule.getRuleValue().equals(Units.NOTHING.getValue());
  }
}
