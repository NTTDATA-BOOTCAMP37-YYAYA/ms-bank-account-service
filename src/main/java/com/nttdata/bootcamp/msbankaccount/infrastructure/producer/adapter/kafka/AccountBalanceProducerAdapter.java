package com.nttdata.bootcamp.msbankaccount.infrastructure.producer.adapter.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.nttdata.bootcamp.msbankaccount.application.outgoing.SendAccountBalancePort;
import com.nttdata.bootcamp.msbankaccount.domain.model.AccountBalance;

import lombok.RequiredArgsConstructor;

/**.*/
@Component
@RequiredArgsConstructor
public class AccountBalanceProducerAdapter implements SendAccountBalancePort {
  
  final  Logger logger = LoggerFactory.getLogger(AccountBalanceProducerAdapter.class);
  
  @Value("${kafka.topic.bank.account-balance.create.name}")
  private String topic;

  private  final KafkaTemplate<String, AccountBalance> kafkaTemplate;
  
  @Override
  public void sendAccountBalance(AccountBalance walletBalance) {
    
    ListenableFuture<SendResult<String, AccountBalance>> future = kafkaTemplate.send(this.topic, walletBalance);
    
    future.addCallback(new ListenableFutureCallback<SendResult<String, AccountBalance>>() {

      @Override
      public void onSuccess(SendResult<String, AccountBalance> result) {
        logger.info("Message {} has been sent", result);
      }

      @Override
      public void onFailure(Throwable ex) {
        logger.error("Something went wrong with the account balance");
        
      }

    });
  }

}
