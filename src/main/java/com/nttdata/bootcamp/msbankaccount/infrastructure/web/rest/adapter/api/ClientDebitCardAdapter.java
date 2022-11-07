package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.msbankaccount.application.outgoing.FindDebitCardByNumberPort;
import com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.enums.HttpStatusCodes;
import com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.response.DebitCard;
import com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.response.ResponseDebitCard;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

/**.*/
@Component
public class ClientDebitCardAdapter implements FindDebitCardByNumberPort {

  final  Logger logger = LoggerFactory.getLogger(ClientDebitCardAdapter.class);

  @Value("${client.bank.debitcard.url}")
  private String url;

  private WebClient client = WebClient.create(url);

  @Override
  @CircuitBreaker(name = "", fallbackMethod = "findDebitCardByNumberAlternative")
  public Mono<DebitCard> findDebitCardByNumber(String debitCardNumber) {
    Mono<DebitCard> response = client.get()
        .uri(url.concat("/findDebitCardByNumber/{debitCardNumber}"), debitCardNumber)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse -> 
        Mono.error(new Exception("Error 400 findDebitCardByNumber")))
        .onStatus(HttpStatus::is5xxServerError, clientResponse ->  
        Mono.error(new Exception("Error 500 findDebitCardByNumber")))
        .toEntity(ResponseDebitCard.class)
        .flatMap(r -> r.getBody().getHttpStatus() == HttpStatusCodes.STATUS_NO_FOUND.getValue() 
            ? Mono.error(new Exception("Error 404 findDebitCardByNumber"))
            : Mono.just(r.getBody().getDebitCard()));
    return response;
  }
  

  public Mono<DebitCard> findDebitCardByNumberAlternative(String debitCardNumber, Exception e) {
    return Mono.error(new Exception("Error on findDebitCardByNumber"));
  }


}
