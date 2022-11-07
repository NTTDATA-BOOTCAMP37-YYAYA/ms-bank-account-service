package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.domain.enums.Units;
import com.nttdata.bootcamp.msbankaccount.domain.model.Account;
import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;

import reactor.core.publisher.Flux;

/**.*/
public interface FindAccounstByCustIdAndProdIdPort {

  Flux<Account> findAccountsByCustIdAndProdId(String customerId, String productId);
  
  /**.*/
  static Boolean validateMaxQuantityProduct(Rule rule,  long quantityProduct) {

    return (rule.getRuleValue().matches("[0-9]+") 
            && Long.valueOf(quantityProduct)
            .compareTo(Long.parseLong(rule.getRuleValue())) < 0) 
            || rule.getRuleValue().equals(Units.MANY.getValue());
  }
  
}
