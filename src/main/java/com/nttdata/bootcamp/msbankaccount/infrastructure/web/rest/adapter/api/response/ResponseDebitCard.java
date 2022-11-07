package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDebitCard {
  
  private int httpStatus;
  private DebitCard debitCard;
  private String message;

}