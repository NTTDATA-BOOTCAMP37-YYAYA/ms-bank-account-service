package com.nttdata.bootcamp.msbankaccount.infrastructure.persistence.adapter;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.bootcamp.msbankaccount.infrastructure.persistence.entity.AccountEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**.*/
public interface ReactiveMongoAccountRepository extends ReactiveMongoRepository
                                                        <AccountEntity, String> {
  
  @Query("{'customerId': ?0,'productId' : ?1, 'accountState' : ?2}")
  public Flux<AccountEntity> findAccountByCustIdAndProdId(String customerId, 
                                                          String productId,String accountState);
  
  @Query("{'accountNumber': ?0, 'accountState' : ?1}")
  public Mono<AccountEntity> findAccountByNumber(String accountNumber,String accountState);

}