package com.nttdata.bootcamp.msbankaccount.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProduct {
  
  private Flux<Account> accounts;
  private Flux<Credit>  creditCards;
  private Flux<Credit>  creditDebts;
}
