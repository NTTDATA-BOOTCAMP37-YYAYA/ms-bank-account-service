package com.nttdata.bootcamp.msbankaccount.application.incoming;

import com.nttdata.bootcamp.msbankaccount.domain.model.Rule;
import java.util.List;
import reactor.core.publisher.Mono;

/**.*/
public interface FindBusinessRulesOfProductUseCase {

  public Mono<List<Rule>> findBusinessRulesOfProduct(String productId,
                                                     String customerTypeId,
                                                     String customerCategoryId,
                                                     String actionId);
}
