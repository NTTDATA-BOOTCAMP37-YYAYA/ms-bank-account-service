package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.controller;


import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.msbankaccount.application.incoming.CreateAccountUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.FindAccountByDebitCardUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.FindAccountByIdUseCase;
import com.nttdata.bootcamp.msbankaccount.application.incoming.FindAccountByNumberUseCase;
import com.nttdata.bootcamp.msbankaccount.domain.model.Account;
import com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.controller.response.ResponseAccount;

import reactor.core.publisher.Mono;

/**.*/
@RestController
@RequestMapping("/account")
public class AccountControllerAdapter {

  final Logger logger = LoggerFactory.getLogger(AccountControllerAdapter.class);
  
  @Autowired
  private  CreateAccountUseCase createAccountUseCase;
  @Autowired
  private  FindAccountByNumberUseCase findAccountByNumberUseCase;
  @Autowired
  private  FindAccountByDebitCardUseCase findAccountByDebitCardUseCase;
  @Autowired
  private  FindAccountByIdUseCase findAccountByIdUseCase;

  /**.*/
  @PostMapping()
  public Mono<ResponseEntity<ResponseAccount>> createAccount(@RequestBody Account account) {
    return createAccountUseCase.createAccount(account)
    	   .flatMap(newAccount ->Mono.just(new ResponseEntity<ResponseAccount>(
  	               new ResponseAccount(HttpStatus.SC_OK, newAccount, "Account has beean created"),
  	               null, HttpStatus.SC_OK)))
           .switchIfEmpty(Mono.just(new ResponseEntity<ResponseAccount>(
	   	               new ResponseAccount(HttpStatus.SC_NOT_FOUND, null, "Account has not been found"),
	   	               null, HttpStatus.SC_NOT_FOUND)))
		   .onErrorResume(e->Mono.just(new ResponseEntity<ResponseAccount>(
	                     new ResponseAccount(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage()),
	                     null, HttpStatus.SC_INTERNAL_SERVER_ERROR)));

  }
  
  
  /**.*/
  @GetMapping("/findAccountByNumber/{accountNumber}")
  public Mono<ResponseEntity<ResponseAccount>> findAccountByNumber( @PathVariable("accountNumber") String accountNumber) {
    return findAccountByNumberUseCase.findAccountByNumber(accountNumber)
        .flatMap(account -> 
              Mono.just(new ResponseEntity<ResponseAccount>(
              new ResponseAccount(HttpStatus.SC_CREATED, account, "Account has been found"),
              null, HttpStatus.SC_CREATED)))
        .switchIfEmpty(Mono.just(new ResponseEntity<ResponseAccount>(
	               new ResponseAccount(HttpStatus.SC_NOT_FOUND, null, "Account has not been found"),
	               null, HttpStatus.SC_NOT_FOUND)))
        .onErrorResume(e->Mono.just(new ResponseEntity<ResponseAccount>(
                  new ResponseAccount(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage()),
                  null, HttpStatus.SC_INTERNAL_SERVER_ERROR)));

  }

  /**.*/
  @GetMapping("/findAccountByDebitCard/{debitCardNumber}")
  public Mono<ResponseEntity<ResponseAccount>> findAccountByDebitCard( @PathVariable("debitCardNumber") String debitCardNumber) {
    //TODO
    return findAccountByDebitCardUseCase.findAccountByDebitCard(debitCardNumber)
        .flatMap(account -> 
              Mono.just(new ResponseEntity<ResponseAccount>(
              new ResponseAccount(HttpStatus.SC_CREATED, account, "Account by credit card number has been found"),
              null, HttpStatus.SC_CREATED)))
         .switchIfEmpty(Mono.just(new ResponseEntity<ResponseAccount>(
	   	               new ResponseAccount(HttpStatus.SC_NOT_FOUND, null, "Account by credit card number has not been found"),
	   	               null, HttpStatus.SC_NOT_FOUND)))
		 .onErrorResume(e->Mono.just(new ResponseEntity<ResponseAccount>(
	                     new ResponseAccount(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage()),
	                     null, HttpStatus.SC_INTERNAL_SERVER_ERROR)));

  }
  

  /**.*/
  @GetMapping("/findAccountById/{accountId}")
  public Mono<ResponseEntity<ResponseAccount>> findAccountById( @PathVariable("accountId") String accountId) {
    //TODO
    return findAccountByIdUseCase.findAccountById(accountId)
        .flatMap(account -> 
              Mono.just(new ResponseEntity<ResponseAccount>(
              new ResponseAccount(HttpStatus.SC_CREATED, account, "Account by Id has been found"),
              null, HttpStatus.SC_CREATED)))
         .switchIfEmpty(Mono.just(new ResponseEntity<ResponseAccount>(
	   	               new ResponseAccount(HttpStatus.SC_NOT_FOUND, null, "Account by Id  has not been found"),
	   	               null, HttpStatus.SC_NOT_FOUND)))
		 .onErrorResume(e->Mono.just(new ResponseEntity<ResponseAccount>(
	                     new ResponseAccount(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage()),
	                     null, HttpStatus.SC_INTERNAL_SERVER_ERROR)));

  }
  

}
