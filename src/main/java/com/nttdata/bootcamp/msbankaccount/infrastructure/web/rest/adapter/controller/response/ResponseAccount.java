package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.controller.response;

import com.nttdata.bootcamp.msbankaccount.domain.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccount {
  
  private int httpStatus;
  private Account account;
  private String message;

}