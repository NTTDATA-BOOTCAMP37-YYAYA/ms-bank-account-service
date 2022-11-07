package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;
import java.util.List;
import reactor.core.publisher.Mono;

/**.*/
public interface FindBusinessRulesOfProductPort {

  public  Mono<List<Rule>> findBusinessRulesOfProduct(String productId,
                                                      String customerTypeId, 
                                                      String customerCategoryId,
                                                      String actionId);
  
  static Boolean compare(List<Rule> rules, String ruleValueEnum) {
    return rules.stream().filter(rule -> rule.getRuleId()
        .equals(ruleValueEnum))
        .count() > 0L;
  }
}
