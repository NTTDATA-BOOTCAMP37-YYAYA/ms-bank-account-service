package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api;

import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindCreditCardsCustomerPort;
import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindCreditDebtsCustomerPort;
import com.nttdata.bootcamp.msbankaccount.domain.model.Credit;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**.*/
@Component
public class ClientCreditAdapter implements FindCreditDebtsCustomerPort,
                                            FindCreditCardsCustomerPort {
  
  final  Logger logger = LoggerFactory.getLogger(ClientCreditAdapter.class);

  @Value("${client.bank.credit.url}")
  private String url;

  private WebClient client = WebClient.create(url);

  @Override
  @CircuitBreaker(name="parameter-service", fallbackMethod ="findCreditDebtsCustomerAlternative" )
  public Flux<Credit> findCreditDebtsCustomer(String customerId) {
    
    Flux<Credit> response = client.get()
         .uri(url.concat("/creditDebts/{customerId}"), customerId)
         .retrieve()
         .onStatus(HttpStatus::is4xxClientError, clientResponse -> 
         Mono.error(new Exception("Error 400")))
         .onStatus(HttpStatus::is5xxServerError, clientResponse ->  
         Mono.error(new Exception("Error 500")))
                   .bodyToFlux(Credit.class);
      
    return response;

  }


  @Override
  @CircuitBreaker(name="parameter-service", fallbackMethod ="findCreditCardsCustomerAlternative" )
  public Flux<Credit> findCreditCardsCustomer(String customerId) {
    
    Flux<Credit> response = client.get()
        .uri(url.concat("/creditCards/{customerId}"), customerId)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse -> 
        Mono.error(new Exception("Error 400")))
        .onStatus(HttpStatus::is5xxServerError, clientResponse ->  
        Mono.error(new Exception("Error 500")))
        .bodyToFlux(Credit.class);
    return response;

  }

  public Flux<Credit> findCreditDebtsCustomerAlternative(String customerId,Exception e) {
    //TODO
    return Flux.error(new Exception("Error on findCreditDebtsCustomer"));
  }
  
  public Flux<Credit> findCreditCardsCustomerAlternative(String customerId,Exception e) {
    //TODO
    return Flux.error(new Exception("Error on findCreditCardsCustomer"));
  }

}
